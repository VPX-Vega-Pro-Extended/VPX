package com.vepro.code

import android.content.Context
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Keeps self-repair sessions alive across multiple tool calls.
 *
 * A session owns one isolated copy of the original workspace.
 *
 * IMPORTANT:
 * Nothing in this class writes to the original workspace.
 */
class RepairSessionManager(context: Context) {

    private val appContext = context.applicationContext

    private val repair = SelfRepair(appContext)

    private val sessions =
        ConcurrentHashMap<String, SelfRepair.Session>()

    /**
     * Creates a new isolated repair session.
     */
    @Synchronized
    fun create(sourceRoot: File): SelfRepair.Session {
        val session = repair.createSession(sourceRoot)

        sessions[session.id] = session

        return session
    }

    /**
     * Returns an existing session.
     */
    fun get(id: String): SelfRepair.Session? {
        return sessions[id]
    }

    /**
     * Returns an existing session or throws.
     */
    fun require(id: String): SelfRepair.Session {
        return sessions[id]
            ?: throw IllegalArgumentException(
                "Unknown self-repair session: $id"
            )
    }

    /**
     * Removes a session and destroys its sandbox.
     */
    @Synchronized
    fun destroy(id: String): Boolean {
        val session = sessions.remove(id) ?: return false

        return try {
            repair.destroy(session)
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Lists active sessions.
     */
    fun list(): List<String> {
        return sessions.keys().toList().sorted()
    }

    /**
     * Reads a file from an active session.
     */
    fun read(id: String, path: String): String {
        return repair.read(
            require(id),
            path
        )
    }

    /**
     * Writes only to an active sandbox.
     */
    fun write(
        id: String,
        path: String,
        content: String
    ) {
        repair.write(
            require(id),
            path,
            content
        )
    }

    /**
     * Creates a directory in an active sandbox.
     */
    fun mkdir(
        id: String,
        path: String
    ): File {
        return repair.mkdir(
            require(id),
            path
        )
    }

    /**
     * Lists files in a sandbox.
     */
    fun files(id: String): List<String> {
        return repair.list(
            require(id)
        )
    }

    /**
     * Validates a sandbox.
     */
    fun validate(id: String): SelfRepair.ValidationResult {
        return repair.validate(
            require(id)
        )
    }

    /**
     * Returns the physical sandbox location.
     */
    fun sandboxPath(id: String): String {
        return repair.sandboxPath(
            require(id)
        )
    }

    /**
     * Cleans all active sessions.
     *
     * Called when the application shuts down or when a repair run
     * must be forcefully abandoned.
     */
    @Synchronized
    fun destroyAll() {
        val ids = sessions.keys().toList()

        for (id in ids) {
            destroy(id)
        }
    }
}