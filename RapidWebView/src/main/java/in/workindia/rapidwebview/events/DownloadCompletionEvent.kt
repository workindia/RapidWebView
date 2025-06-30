package `in`.workindia.rapidwebview.events

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.EVENT_DOWNLOAD_LISTENER
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.EVENT_KEY
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.FAILURE
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.KEY_DOWNLOAD_COMPLETE
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.KEY_DOWNLOAD_ID
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.STATUS
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.SUCCESS
import `in`.workindia.rapidwebview.broadcast.BroadcastActionHandler.generateJavaScriptEvent

class DownloadCompletionEvent(private val context: Context, private val intent: Intent) : CustomJSEvent {

    override fun generateJavaScript(): String {
        val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
        var status = if (downloadId != -1L) SUCCESS else FAILURE

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = downloadManager.query(query)

        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if (columnIndex != -1) {
                val statusIndex = cursor.getInt(columnIndex)
                status = if (statusIndex == DownloadManager.STATUS_SUCCESSFUL) SUCCESS else FAILURE
            }
        }
        cursor.close()

        return generateJavaScriptEvent(
            EVENT_DOWNLOAD_LISTENER, mapOf(
                EVENT_KEY to KEY_DOWNLOAD_COMPLETE,
                STATUS to status,
                KEY_DOWNLOAD_ID to "$downloadId"
            )
        )
    }
}