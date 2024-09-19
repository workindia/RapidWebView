package `in`.workindia.rapidwebview.events

import android.content.Intent
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.EVENT_PERMISSION_LISTENER
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.FAILURE
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.PERMISSION_LIST_KEY
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.STATUS
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.SUCCESS
import `in`.workindia.rapidwebview.utils.LocalBroadCastActionUtility.generateJavaScriptEvent

class PermissionResultEvent(private val intent: Intent) : CustomJSEvent {

    override fun generateJavaScript(): String {
        val permissionList =
            intent.getStringArrayExtra(BroadcastConstants.PERMISSION_LIST) ?: emptyArray()
        val status =
            if (intent.getStringExtra(BroadcastConstants.PERMISSION) == SUCCESS) SUCCESS else FAILURE

        return generateJavaScriptEvent(
            EVENT_PERMISSION_LISTENER, mapOf(
                STATUS to status,
                PERMISSION_LIST_KEY to permissionList.joinToString(", ")
            )
        )
    }
}