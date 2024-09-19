package `in`.workindia.rapidwebview.events

import android.content.Intent
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import `in`.workindia.rapidwebview.utils.LocalBroadCastActionUtility.generateJavaScriptEvent

class UploadCompletionEvent(private val intent: Intent) : CustomJSEvent {

    override fun generateJavaScript(): String {
        val uploadUrl = intent.getStringExtra(BroadcastConstants.UPLOADED_URL) ?: ""
        val fileName = intent.getStringExtra(BroadcastConstants.UPLOADED_FILE_NAME) ?: ""
        val status = if (intent.getStringExtra(BroadcastConstants.UPLOAD) == BroadcastConstants.SUCCESS) "success" else "failure"

        return generateJavaScriptEvent(
            "rapid-web-view-upload-listener", mapOf(
                "status" to status,
                "uploadUrl" to uploadUrl,
                "uploadFileName" to fileName
            )
        )
    }
}