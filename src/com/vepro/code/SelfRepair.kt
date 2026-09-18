package com.vepro.code

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID

/**
 * Isolated workspace for agent self-repair.
 *
 * IMPORTANT:
 * This class NEVER modifies the original workspace.
 * Every repair starts from a complete copy of the original workspace.
 */
class SelfRepair(private val context: Context) {

    data class Session(
        val id: String,
        val sourceRoot: File,
        val sandboxRoot: File
    )

    private val sessionsRoot: File
        get() = File(context.cacheDir, "vega_self_repair")

    /**
     * Creates a completely isolated copy of the current workspace.
     */
    fun createSession(sourceRoot: File): Session {
        require(sourceRoot.exists()) {
            "Source workspace does not exist: ${sourceRoot.absolutePath}"
        }

        val id = "repair_" +
            System.currentTimeMillis() +
            "_" +
            UUID.randomUUID().toString().substring(0, 8)

        val sandbox = File(sessionsRoot, id)

        if (!sandbox.mkdirs() && !sandbox.isDirectory) {
            throw IllegalStateException(
                "Could not create repair sandbox: ${sandbox.absolutePath}"
            )
        }

        copyDirectory(sourceRoot, sandbox)

        return Session(
            id = id,
            sourceRoot = sourceRoot,
            sandboxRoot = sandbox
        )
    }

    /**
     * Returns a path inside the sandbox.
     *
     * Any attempt to escape the sandbox is rejected.
     */
    fun resolve(session: Session, path: String): File {
        val requested = if (File(path).isAbsolute) {
            File(path)
        } else {
            File(session.sandboxRoot, path)
        }

        val root = session.sandboxRoot.canonicalFile
        val candidate = requested.canonicalFile

        if (candidate != root &&
            !candidate.path.startsWith(root.path + File.separator)
        ) {
            throw SecurityException(
                "Path escapes repair sandbox: ${candidate.absolutePath}"
            )
        }

        return candidate
    }

    /**
     * Reads a file from the isolated repair workspace.
     */
    fun read(session: Session, path: String): String {
        val file = resolve(session, path)

        if (!file.exists()) {
            throw IllegalArgumentException(
                "File does not exist in sandbox: $path"
            )
        }

        if (!file.isFile) {
            throw IllegalArgumentException(
                "Path is not a file: $path"
            )
        }

        return file.readText(Charsets.UTF_8)
    }

    /**
     * Writes a file ONLY inside the isolated repair workspace.
     */
    fun write(session: Session, path: String, content: String) {
        val file = resolve(session, path)

        file.parentFile?.mkdirs()

        val temporary = File(
            file.parentFile ?: session.sandboxRoot,
            ".${file.name}.repair_tmp"
        )

        try {
            FileOutputStream(temporary).use { output ->
                output.write(content.toByteArray(Charsets.UTF_8))
                output.flush()
                output.fd.sync()
            }

            if (file.exists() && !file.delete()) {
                throw IllegalStateException(
                    "Could not replace sandbox file: $path"
                )
            }

            if (!temporary.renameTo(file)) {
                throw IllegalStateException(
                    "Could not commit sandbox file: $path"
                )
            }
        } finally {
            if (temporary.exists()) {
                temporary.delete()
            }
        }
    }

    /**
     * Creates a directory inside the sandbox.
     */
    fun mkdir(session: Session, path: String): File {
        val dir = resolve(session, path)

        if (!dir.exists() && !dir.mkdirs()) {
            throw IllegalStateException(
                "Could not create sandbox directory: $path"
            )
        }

        return dir
    }

    /**
     * Lists sandbox files recursively.
     */
    fun list(session: Session): List<String> {
        val root = session.sandboxRoot

        if (!root.exists()) {
            return emptyList()
        }

        return root.walkTopDown()
            .filter { it != root }
            .map {
                it.relativeTo(root).path
            }
            .sorted()
            .toList()
    }

    /**
     * Basic structural validation.
     *
     * This intentionally does not claim that Kotlin compiles.
     * It catches common corruption before anything is considered usable.
     */
    fun validate(session: Session): ValidationResult {
        val problems = ArrayList<String>()

        if (!session.sandboxRoot.exists()) {
            problems.add("Sandbox directory disappeared.")
            return ValidationResult(false, problems)
        }

        val files = session.sandboxRoot.walkTopDown()
            .filter { it.isFile }
            .toList()

        if (files.isEmpty()) {
            problems.add("Sandbox is empty.")
        }

        for (file in files) {
            if (file.length() > MAX_FILE_SIZE) {
                problems.add(
                    "File exceeds safety limit: " +
                        file.relativeTo(session.sandboxRoot).path
                )
            }
        }

        validateKotlinFiles(session, files, problems)

        return ValidationResult(
            valid = problems.isEmpty(),
            problems = problems
        )
    }

    private fun validateKotlinFiles(
        session: Session,
        files: List<File>,
        problems: MutableList<String>
    ) {
        for (file in files) {
            if (!file.name.endsWith(".kt")) {
                continue
            }

            try {
                val text = file.readText(Charsets.UTF_8)

                if (text.contains("\u0000")) {
                    problems.add(
                        "NUL byte found in Kotlin file: " +
                            file.relativeTo(session.sandboxRoot).path
                    )
                }

                val balance = checkBraces(text)

                if (!balance.valid) {
                    problems.add(
                        "${file.relativeTo(session.sandboxRoot).path}: " +
                            balance.message
                    )
                }
            } catch (e: Exception) {
                problems.add(
                    "Could not inspect Kotlin file " +
                        file.relativeTo(session.sandboxRoot).path +
                        ": " +
                        e.message
                )
            }
        }
    }

    private data class BalanceResult(
        val valid: Boolean,
        val message: String
    )

    private fun checkBraces(text: String): BalanceResult {
        var braces = 0
        var parentheses = 0
        var brackets = 0

        var inString = false
        var inChar = false
        var inLineComment = false
        var inBlockComment = false
        var escaped = false

        var i = 0

        while (i < text.length) {
            val c = text[i]
            val next = if (i + 1 < text.length) text[i + 1] else '\u0000'

            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false
                }
                i++
                continue
            }

            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false
                    i += 2
                    continue
                }
                i++
                continue
            }

            if (inString) {
                if (escaped) {
                    escaped = false
                } else if (c == '\\') {
                    escaped = true
                } else if (c == '"') {
                    inString = false
                }
                i++
                continue
            }

            if (inChar) {
                if (escaped) {
                    escaped = false
                } else if (c == '\\') {
                    escaped = true
                } else if (c == '\'') {
                    inChar = false
                }
                i++
                continue
            }

            if (c == '/' && next == '/') {
                inLineComment = true
                i += 2
                continue
            }

            if (c == '/' && next == '*') {
                inBlockComment = true
                i += 2
                continue
            }

            if (c == '"') {
                inString = true
                i++
                continue
            }

            if (c == '\'') {
                inChar = true
                i++
                continue
            }

            when (c) {
                '{' -> braces++
                '}' -> {
                    braces--
                    if (braces < 0) {
                        return BalanceResult(
                            false,
                            "unbalanced }"
                        )
                    }
                }

                '(' -> parentheses++
                ')' -> {
                    parentheses--
                    if (parentheses < 0) {
                        return BalanceResult(
                            false,
                            "unbalanced )"
                        )
                    }
                }

                '[' -> brackets++
                ']' -> {
                    brackets--
                    if (brackets < 0) {
                        return BalanceResult(
                            false,
                            "unbalanced ]"
                        )
                    }
                }
            }

            i++
        }

        if (inString) {
            return BalanceResult(false, "unterminated string")
        }

        if (inChar) {
            return BalanceResult(false, "unterminated character literal")
        }

        if (inBlockComment) {
            return BalanceResult(false, "unterminated block comment")
        }

        if (braces != 0) {
            return BalanceResult(false, "unbalanced braces")
        }

        if (parentheses != 0) {
            return BalanceResult(false, "unbalanced parentheses")
        }

        if (brackets != 0) {
            return BalanceResult(false, "unbalanced brackets")
        }

        return BalanceResult(true, "OK")
    }

    /**
     * Deletes a repair session.
     *
     * This only touches the temporary repair area.
     */
    fun destroy(session: Session) {
        if (!session.sandboxRoot.exists()) {
            return
        }

        deleteRecursively(session.sandboxRoot)
    }

    /**
     * Returns the final sandbox path for inspection/debugging.
     */
    fun sandboxPath(session: Session): String =
        session.sandboxRoot.absolutePath

    private val EXCLUDED_DIRECTORIES = setOf(
        ".git",
        ".gradle",
        ".idea",
        "build",
        "captures",
        "node_modules",
        "__pycache__",
        ".venv",
        "venv"
    )

    private val EXCLUDED_FILES = setOf(
        ".DS_Store"
    )

    private val EXCLUDED_EXTENSIONS = setOf(
        ".apk",
        ".aab",
        ".mp4",
        ".mkv",
        ".avi",
        ".mov",
        ".webm",
        ".zip",
        ".rar",
        ".7z",
        ".tar",
        ".gz",
        ".iso",
        ".img"
    )

    private fun copyDirectory(source: File, destination: File) {
        if (!source.exists()) {
            throw IllegalArgumentException(
                "Source does not exist: ${source.absolutePath}"
            )
        }

        if (shouldExclude(source)) {
            return
        }

        if (source.isDirectory) {
            if (!destination.exists() && !destination.mkdirs()) {
                throw IllegalStateException(
                    "Could not create directory: ${destination.absolutePath}"
                )
            }

            val children = source.listFiles() ?: return

            for (child in children) {
                if (shouldExclude(child)) {
                    continue
                }

                val target = File(destination, child.name)

                if (child.isDirectory) {
                    copyDirectory(child, target)
                } else {
                    copyFile(child, target)
                }
            }
        } else {
            copyFile(source, destination)
        }
    }

    private fun shouldExclude(file: File): Boolean {
        val name = file.name

        if (name in EXCLUDED_DIRECTORIES) {
            return true
        }

        if (name in EXCLUDED_FILES) {
            return true
        }

        return EXCLUDED_EXTENSIONS.any { extension ->
            name.endsWith(extension, ignoreCase = true)
        }
    }

    private fun copyFile(source: File, destination: File) {
        destination.parentFile?.mkdirs()

        FileInputStream(source).use { input ->
            FileOutputStream(destination).use { output ->
                val buffer = ByteArray(BUFFER_SIZE)

                while (true) {
                    val count = input.read(buffer)

                    if (count < 0) {
                        break
                    }

                    output.write(buffer, 0, count)
                }

                output.flush()
            }
        }
    }

    private fun deleteRecursively(file: File) {
        if (file.isDirectory) {
            file.listFiles()?.forEach {
                deleteRecursively(it)
            }
        }

        file.delete()
    }

    data class CommandResult(
        val exitCode: Int,
        val stdout: String,
        val stderr: String,
        val timedOut: Boolean
    )


    private fun validateCommand(command: List<String>) {
        for (part in command) {
            if (part.indexOf('\u0000') >= 0) {
                throw SecurityException(
                    "Command contains NUL byte"
                )
            }

            if (part.startsWith("/")) {
                throw SecurityException(
                    "Absolute paths are not allowed in self-repair commands"
                )
            }

            if (part == ".." ||
                part.startsWith("../") ||
                part.contains("/../")
            ) {
                throw SecurityException(
                    "Path traversal is not allowed in self-repair commands"
                )
            }
        }
    }

    fun execute(
        session: Session,
        command: List<String>,
        timeoutMs: Long = 30_000L
    ): CommandResult {

        if (command.isEmpty()) {
            throw IllegalArgumentException(
                "Command cannot be empty"
            )
        }
        validateCommand(command)
        
        val processBuilder = ProcessBuilder(command)
            .directory(session.sandboxRoot)
            .redirectErrorStream(false)

        val process = processBuilder.start()

        val stdout = StringBuilder()
        val stderr = StringBuilder()

        val stdoutThread = Thread {
            process.inputStream.bufferedReader().use { reader ->
                reader.forEachLine {
                    if (stdout.length < OUTPUT_LIMIT) {
                        stdout.append(it).append('\n')
                    }
                }
            }
        }

        val stderrThread = Thread {
            process.errorStream.bufferedReader().use { reader ->
                reader.forEachLine {
                    if (stderr.length < OUTPUT_LIMIT) {
                        stderr.append(it).append('\n')
                    }
                }
            }
        }

        stdoutThread.isDaemon = true
        stderrThread.isDaemon = true

        stdoutThread.start()
        stderrThread.start()

        val finished = process.waitFor(
            timeoutMs,
            java.util.concurrent.TimeUnit.MILLISECONDS
        )

        if (!finished) {
            process.destroy()

            try {
                process.waitFor(
                    1000L,
                    java.util.concurrent.TimeUnit.MILLISECONDS
                )
            } catch (_: Exception) {
            }

            if (process.isAlive) {
                process.destroyForcibly()
            }

            return CommandResult(
                exitCode = -1,
                stdout = stdout.toString(),
                stderr = stderr.toString(),
                timedOut = true
            )
        }

        try {
            stdoutThread.join(1000L)
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        try {
            stderrThread.join(1000L)
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        return CommandResult(
            exitCode = process.exitValue(),
            stdout = stdout.toString(),
            stderr = stderr.toString(),
            timedOut = false
        )
    }
    companion object {
        private const val BUFFER_SIZE = 64 * 1024
        private const val MAX_FILE_SIZE = 32L * 1024L * 1024L
        private const val OUTPUT_LIMIT = 256 * 1024
    }

    data class ValidationResult(
        val valid: Boolean,
        val problems: List<String>
    )
}