package `in`.workindia.rapidwebview.events

import android.content.Intent
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import `in`.workindia.rapidwebview.utils.LocalBroadCastActionUtility.generateJavaScriptEvent

class PermissionResultEvent(private val intent: Intent) : CustomJSEvent {

    override fun generateJavaScript(): String {
        val permissionList = intent.getStringArrayExtra(BroadcastConstants.PERMISSION_LIST) ?: emptyArray()
        val status = if (intent.getStringExtra(BroadcastConstants.PERMISSION) == BroadcastConstants.SUCCESS) "success" else "failure"

        return generateJavaScriptEvent(
            "rapid-web-view-permission-listener", mapOf(
                "status" to status,
                "permissionList" to permissionList.joinToString(", ")
            )
        )
    }
}