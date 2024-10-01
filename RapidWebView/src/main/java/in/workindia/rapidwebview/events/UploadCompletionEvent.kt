package `in`.workindia.rapidwebview.events

import android.content.Intent
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.EVENT_UPLOAD_LISTENER
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.FAILURE
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.KEY_UPLOAD_FILE_NAME
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.KEY_UPLOAD_URL
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.STATUS
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.SUCCESS
import `in`.workindia.rapidwebview.utils.LocalBroadcastActionUtility.generateJavaScriptEvent

class UploadCompletionEvent(private val intent: Intent) : CustomJSEvent {

    override fun generateJavaScript(): String {
        val uploadUrl = intent.getStringExtra(BroadcastConstants.UPLOADED_URL) ?: ""
        val fileName = intent.getStringExtra(BroadcastConstants.UPLOADED_FILE_NAME) ?: ""
        val status =
            if (intent.getStringExtra(BroadcastConstants.UPLOAD) == SUCCESS) SUCCESS else FAILURE

        return generateJavaScriptEvent(
            EVENT_UPLOAD_LISTENER, mapOf(
                STATUS to status,
                KEY_UPLOAD_URL to uploadUrl,
                KEY_UPLOAD_FILE_NAME to fileName
            )
        )
    }
}