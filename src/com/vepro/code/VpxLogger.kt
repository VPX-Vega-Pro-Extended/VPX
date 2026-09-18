package com.vepro.code

import android.content.Context
import android.os.Build
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object VpxLogger {

    private const val TAG = "VPX"
    private const val DIR_NAME = "vpx_logs"
    private const val FILE_NAME = "vpx.log"

    private const val MAX_FILE_SIZE = 2L * 1024L * 1024L

    private val lock = Any()

    @Volatile
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext =
            context.applicationContext

        info(
            "VpxLogger",
            "Logger initialized"
        )
    }

    fun debug(
        source: String,
        message: String
    ) {
        write(
            "DEBUG",
            source,
            message,
            null
        )
    }

    fun info(
        source: String,
        message: String
    ) {
        write(
            "INFO",
            source,
            message,
            null
        )
    }

    fun warn(
        source: String,
        message: String
    ) {
        write(
            "WARN",
            source,
            message,
            null
        )
    }

    fun error(
        source: String,
        message: String,
        error: Throwable? = null
    ) {
        write(
            "ERROR",
            source,
            message,
            error
        )
    }

    private fun write(
        level: String,
        source: String,
        message: String,
        error: Throwable?
    ) {
        val timestamp =
            SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS",
                Locale.US
            ).format(Date())

        val line =
            buildString {
                append(timestamp)
                append(" | ")
                append(level)
                append(" | ")
                append(source)
                append(" | ")
                append(message)

                if (error != null) {
                    append(" | ")
                    append(error.javaClass.simpleName)
                    append(": ")
                    append(error.message ?: "")
                }

                append('\n')
            }

        when (level) {
            "DEBUG" ->
                Log.d(
                    TAG,
                    "[$source] $message",
                    error
                )

            "INFO" ->
                Log.i(
                    TAG,
                    "[$source] $message",
                    error
                )

            "WARN" ->
                Log.w(
                    TAG,
                    "[$source] $message",
                    error
                )

            "ERROR" ->
                Log.e(
                    TAG,
                    "[$source] $message",
                    error
                )
        }

        val context =
            appContext ?: return

        synchronized(lock) {
            try {
                val dir =
                    File(
                        context.cacheDir,
                        DIR_NAME
                    )

                if (!dir.exists()) {
                    dir.mkdirs()
                }

                val file =
                    File(
                        dir,
                        FILE_NAME
                    )

                val bytes =
                    line.toByteArray(
                        Charsets.UTF_8
                    )

                if (
                    file.exists() &&
                    file.length() + bytes.size >
                    MAX_FILE_SIZE
                ) {
                    file.delete()
                }

                file.appendBytes(bytes)

            } catch (_: Throwable) {
                // Logging must never crash VPX.
            }
        }
    }

    fun read(): String {
        val context =
            appContext ?: return ""

        return try {
            File(
                context.cacheDir,
                "$DIR_NAME/$FILE_NAME"
            ).takeIf {
                it.exists()
            }?.readText()
                ?: ""

        } catch (_: Throwable) {
            ""
        }
    }

    fun clear() {
        val context =
            appContext ?: return

        synchronized(lock) {
            try {
                File(
                    context.cacheDir,
                    DIR_NAME
                ).deleteRecursively()
            } catch (_: Throwable) {
            }
        }
    }

    fun file(): File? {
        val context =
            appContext ?: return null

        return try {
            val dir =
                File(
                    context.cacheDir,
                    DIR_NAME
                )

            if (!dir.exists()) {
                dir.mkdirs()
            }

            File(
                dir,
                FILE_NAME
            )

        } catch (_: Throwable) {
            null
        }
    }

    fun systemInfo(): String {
        return buildString {
            append("VPX\n")
            append("Android: ")
            append(Build.VERSION.RELEASE)
            append('\n')
            append("SDK: ")
            append(Build.VERSION.SDK_INT)
            append('\n')
            append("Device: ")
            append(Build.MANUFACTURER)
            append(' ')
            append(Build.MODEL)
            append('\n')
        }
    }
}
