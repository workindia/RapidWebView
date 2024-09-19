package `in`.workindia.rapidwebview.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.webkit.WebView
import androidx.annotation.RequiresApi
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import `in`.workindia.rapidwebview.events.DownloadCompletionEvent
import `in`.workindia.rapidwebview.events.PermissionResultEvent
import `in`.workindia.rapidwebview.events.UploadCompletionEvent

object LocalBroadCastActionUtility {

    fun createReceiver(
        action: String,
        onReceive: (context: Context?, intent: Intent, receiver: BroadcastReceiver) -> Unit,
    ): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == action) {
                    onReceive(context, intent, this)
                }
            }
        }
    }

    fun registerReceiver(
        context: Context,
        broadcastReceiver: BroadcastReceiver,
        intentFilter: IntentFilter,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            registerReceiverApi34(
                context,
                broadcastReceiver,
                intentFilter
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiverApi26(
                context,
                broadcastReceiver,
                intentFilter
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun registerReceiverApi34(
        context: Context,
        broadcastReceiver: BroadcastReceiver,
        intentFilter: IntentFilter,
    ) {
        context.registerReceiver(
            broadcastReceiver,
            intentFilter,
            RECEIVER_EXPORTED
        )
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @RequiresApi(Build.VERSION_CODES.O)
    fun registerReceiverApi26(
        context: Context,
        broadcastReceiver: BroadcastReceiver,
        intentFilter: IntentFilter,
    ) {
        context.registerReceiver(
            broadcastReceiver,
            intentFilter
        )
    }

    /**
     * Handles [BroadcastConstants.NATIVE_CALLBACK_ACTION] by dispatching the appropriate JavaScript events to the WebView.
     *
     * **Event Dispatch:**
     * - event: `rapid-web-view-upload-listener` event with the payload:
     *        `{ detail: { status: "success|failure", uploadUrl: "uploadUrl", uploadFileName: "uploadFileName" } }`
     * - event: `rapid-web-view-permission-listener` event with the payload:
     *        `{ detail: { status: "success|failure", permissionList: "permission1, permission2" } }`
     *
     * @param intent The intent containing callback action data.
     * @param webView The WebView to which the JavaScript will be dispatched.
     */
    fun handleNativeCallbackAction(webView: WebView, intent: Intent) {
        val action = when {
            intent.hasExtra(BroadcastConstants.PERMISSION) -> {
                PermissionResultEvent(intent)
            }

            intent.hasExtra(BroadcastConstants.UPLOAD) -> {
                UploadCompletionEvent(intent)
            }

            else -> null
        }

        action?.let {
            val javaScript = it.generateJavaScript()
            dispatchJavaScript(javaScript, webView)
        }
    }


    /**
     * Dispatches the `rapid-web-view-download-listener` event to the WebView.
     * this method fires a `rapid-web-view-download-listener` event with payload: `{ detail: { status: "success|failure", downloadId: "downloadId" } }`
     *
     * @param webView The WebView to which the JavaScript will be dispatched.
     * @param intent The intent received from BrodCastReceiver.
     */
    fun dispatchDownloadCompletionEvent(webView: WebView, intent: Intent) {
        val action = DownloadCompletionEvent(intent)
        val javaScript = action.generateJavaScript()
        dispatchJavaScript(javaScript, webView)
    }

    /**
     * Generates a JavaScript event string that can be dispatched to the WebView.
     *
     * @param eventName The name of the event to be dispatched.
     * @param details A map containing details to be included in the event.
     * @return A string representing the JavaScript event.
     */
    fun generateJavaScriptEvent(eventName: String, details: Map<String, String>): String {
        val detailsJson = details.entries.joinToString(",") { (key, value) ->
            "'$key' : '$value'"
        }
        return "window.dispatchEvent(new CustomEvent('$eventName', { detail: { $detailsJson } }))"
    }

    /**
     * Dispatches the provided JavaScript code to the specified WebView.
     *
     * @param javaScript The JavaScript code to be executed.
     * @param webView The WebView in which the JavaScript will be executed.
     */
    @SuppressLint("ObsoleteSdkInt")
    fun dispatchJavaScript(javaScript: String, webView: WebView) {
        webView.post {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(javaScript, null)
            } else {
                webView.loadUrl("javascript:(function(){$javaScript})()")
            }
        }
    }
}