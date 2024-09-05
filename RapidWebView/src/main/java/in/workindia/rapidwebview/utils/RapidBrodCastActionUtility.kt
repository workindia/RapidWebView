package `in`.workindia.rapidwebview.utils

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.webkit.WebView
import android.widget.Toast
import `in`.workindia.rapidwebview.activities.service.UploadService
import `in`.workindia.rapidwebview.RapidWebViewJSInterface
import `in`.workindia.rapidwebview.datamodels.DownloadLocation
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.DOWNLOAD_LOCATION
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.DOWNLOAD_URL
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.FILE_NAME
import `in`.workindia.rapidwebview.utils.Utils.toDownloadLocation

object RapidBrodCastActionUtility {

    /**
     * Handles [BroadcastConstants.NATIVE_CALLBACK_ACTION] action by determining the appropriate JavaScript
     * event to generate and dispatch based on the provided intent's status.
     * event: "rapid-web-view-upload-listener" for upload success or failure
     * event: "rapid-web-view-upload-listener" for permissions success or failure
     *
     * @see [UploadService]
     * @see [RapidWebViewJSInterface.broadcastReceiverForNativeCallbacks]
     * @see [permissionSuccessHelper]
     * @param intent The intent containing callback action data.
     * @param webView The WebView to which the JavaScript will be dispatched.
     * @param context The context in which the function is executed. Can be null.
     */
    fun handleNativeCallbackAction(intent: Intent, webView: WebView, context: Context?) {
        val permissionStatus = intent.getStringExtra(BroadcastConstants.PERMISSION)
        val uploadStatus = intent.getStringExtra(BroadcastConstants.UPLOAD)
        val uploadUrl = intent.getStringExtra(BroadcastConstants.UPLOADED_URL) ?: ""
        val fileName = intent.getStringExtra(BroadcastConstants.UPLOADED_FILE_NAME) ?: ""
        val permissionList =
            intent.getStringArrayExtra(BroadcastConstants.PERMISSION_LIST) ?: emptyArray()

        val javaScript = when {
            uploadStatus.equals(BroadcastConstants.SUCCESS, ignoreCase = true) -> {
                generateJavaScriptEvent(
                    "rapid-web-view-upload-listener", mapOf(
                        "status" to "success",
                        "uploadUrl" to uploadUrl,
                        "uploadFileName" to fileName
                    )
                )
            }

            uploadStatus.equals(BroadcastConstants.FAILURE, ignoreCase = true) -> {
                generateJavaScriptEvent(
                    "rapid-web-view-upload-listener", mapOf(
                        "status" to "failure"
                    )
                )
            }

            permissionStatus.equals(BroadcastConstants.SUCCESS, ignoreCase = true) -> {
                permissionSuccessHelper(permissionList, intent, context)
            }

            permissionStatus.equals(BroadcastConstants.FAILURE, ignoreCase = true) -> {
                generateJavaScriptEvent(
                    "rapid-web-view-permission-listener", mapOf(
                        "status" to "failure"
                    )
                )
            }

            else -> ""
        }

        if (javaScript.isNotBlank()) {
            dispatchJavaScript(javaScript, webView)
        }
    }


    /**
     * Handles the action to be taken when a download is complete.
     * Displays a toast message and opens the downloaded file.
     *
     * @param context The context received from BrodCastReceiver.
     * @param intent The intent received from BrodCastReceiver.
     */
    fun handleDownloadCompleteAction(context: Context?, intent: Intent) {
        Toast.makeText(context, "Download Complete", Toast.LENGTH_SHORT).show()
        val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        context?.let {
            RapidDownLoadUtility.openDownLoadedFile(it, downloadId)
        }
    }


    /**
     * Helper function to handle successful permission grants. It checks if
     * the required permissions for reading and writing files are granted,
     * and if so, proceeds with checking the [DOWNLOAD_URL] from intent and initiates a file download.
     *
     * we ask permissions when [RapidWebViewJSInterface.downLoadFileLocally] and [RapidWebViewJSInterface.downLoadFileLocally] are called. we continue the download only if the permissions are granted.
     * @see [RapidWebViewJSInterface.downLoadFileLocally]
     * @see [RapidWebViewJSInterface.downLoadAndOpenFile]
     *
     * @param permissionList List of granted permissions.
     * @param intent The intent received from BrodCastReceiver.
     * @param context The context received from BrodCastReceiver.
     * @return A JavaScript event string indicating the success of the permission grant.
     */
    private fun permissionSuccessHelper(
        permissionList: Array<String>,
        intent: Intent,
        context: Context?,
    ): String {
        when {
            permissionList.contentEquals(RapidPermissionHelper.requiredPermissionsForReadAndWrite()) -> {
                checkAndDownloadFile(intent, context)
            }
        }

        return generateJavaScriptEvent(
            "rapid-web-view-permission-listener", mapOf(
                "status" to "success",
                "permissionList" to permissionList.joinToString(", ")
            )
        )
    }

    /**
     * Checks the provided intent for a download URL and file name, and
     * initiates a file download using the provided context.
     *
     * @see [RapidDownLoadUtility.startDownload], [permissionSuccessHelper]
     * @param intent The intent containing download data.
     * @param context The context in which the function is executed. Can be null.
     */
    private fun checkAndDownloadFile(intent: Intent, context: Context?) {
        val bundle = intent.extras
        val url: String? = bundle?.getString(DOWNLOAD_URL)
        val fileName: String? = bundle?.getString(FILE_NAME)
        val downloadLocation: DownloadLocation =
            toDownloadLocation(bundle?.getString(DOWNLOAD_LOCATION))

        if (!url.isNullOrBlank() && context != null) {
            RapidDownLoadUtility.startDownload(url, context, fileName, downloadLocation)
        }
    }

    /**
     * Generates a JavaScript event string that can be dispatched to the WebView.
     *
     * @param eventName The name of the event to be dispatched Eg: "rapid-web-view-permission-listener".
     * @param details A map containing details to be included in the event Eg: { status: "success|failure", permissionList: "permission1, permission2" }.
     * @return A string representing the JavaScript event.
     *
     * @see [RapidWebViewJSInterface.downLoadFileLocally]
     * @see [handleNativeCallbackAction]
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                webView.evaluateJavascript(javaScript, null)
            } else {
                webView.loadUrl("javascript:(function(){$javaScript})()")
            }
        }
    }
}