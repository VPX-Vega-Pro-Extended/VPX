package com.vepro.code

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.webkit.MimeTypeMap
import java.io.File

class VpxLogProvider : ContentProvider() {

    companion object {
        private const val PATH_LOG = "log"

        fun uriForLog(): Uri {
            return Uri.parse(
                "content://com.vepro.code.vpxlog/$PATH_LOG"
            )
        }
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun openFile(
        uri: Uri,
        mode: String
    ): ParcelFileDescriptor? {
        if (uri.path != "/$PATH_LOG") {
            throw IllegalArgumentException("Unknown log URI: $uri")
        }

        if (mode != "r") {
            throw IllegalArgumentException("Log provider is read-only")
        }

        val file = VpxLogger.file()
            ?: throw IllegalStateException("Log file unavailable")

        if (!file.exists() || !file.isFile) {
            throw IllegalStateException("Log file does not exist")
        }

        return ParcelFileDescriptor.open(
            file,
            ParcelFileDescriptor.MODE_READ_ONLY
        )
    }

    override fun getType(uri: Uri): String? {
        return if (uri.path == "/$PATH_LOG") {
            "text/plain"
        } else {
            null
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        if (uri.path != "/$PATH_LOG") {
            return null
        }

        val columns = projection ?: arrayOf(
            "_display_name",
            "_size"
        )

        val cursor = MatrixCursor(columns)
        val file = VpxLogger.file()

        val row = Array<Any?>(columns.size) { index ->
            when (columns[index]) {
                "_display_name" -> "vpx.log"
                "_size" -> file?.takeIf { it.exists() }?.length() ?: 0L
                else -> null
            }
        }

        cursor.addRow(row)
        return cursor
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri? {
        throw UnsupportedOperationException("Read-only provider")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException("Read-only provider")
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException("Read-only provider")
    }
}
