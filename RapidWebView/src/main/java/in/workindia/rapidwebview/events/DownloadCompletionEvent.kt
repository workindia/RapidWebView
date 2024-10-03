package `in`.workindia.rapidwebview.events

import android.app.DownloadManager
import android.content.Intent
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.EVENT_DOWNLOAD_LISTENER
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.EVENT_KEY
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.FAILURE
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.KEY_DOWNLOAD_COMPLETE
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.KEY_DOWNLOAD_ID
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.STATUS
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.SUCCESS
import `in`.workindia.rapidwebview.broadcast.BroadcastActionHandler.generateJavaScriptEvent

class DownloadCompletionEvent(private val intent: Intent) : CustomJSEvent {

    override fun generateJavaScript(): String {
        val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
        val status = if (downloadId != -1L) SUCCESS else FAILURE

        return generateJavaScriptEvent(
            EVENT_DOWNLOAD_LISTENER, mapOf(
                EVENT_KEY to KEY_DOWNLOAD_COMPLETE,
                STATUS to status,
                KEY_DOWNLOAD_ID to "$downloadId"
            )
        )
    }
}