package `in`.workindia.rapidwebview.events

import android.app.DownloadManager
import android.content.Intent
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.EVENT_DOWNLOAD_LISTENER
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.KEY_DOWNLOAD_COMPLETE
import `in`.workindia.rapidwebview.utils.LocalBroadCastActionUtility.generateJavaScriptEvent

class DownloadCompletionEvent(private val intent: Intent) : CustomJSEvent {

    override fun generateJavaScript(): String {
        val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
        val status = if (downloadId != -1L) "success" else "failure"

        return generateJavaScriptEvent(
            EVENT_DOWNLOAD_LISTENER, mapOf(
                "eventKey" to KEY_DOWNLOAD_COMPLETE,
                "status" to status,
                "downloadId" to "$downloadId"
            )
        )
    }
}