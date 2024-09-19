package `in`.workindia.rapidwebview

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.TextUtils
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import `in`.workindia.rapidwebview.activities.PermissionActivity
import `in`.workindia.rapidwebview.activities.UploadFileActivity
import `in`.workindia.rapidwebview.assetcache.RapidAssetCacheDownloader
import `in`.workindia.rapidwebview.assetcache.RapidStorageUtility
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.PERMISSION_LIST_KEY
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.RATIONAL_TEXT
import `in`.workindia.rapidwebview.utils.DownLoadUtility
import `in`.workindia.rapidwebview.utils.LocalBroadCastActionUtility
import `in`.workindia.rapidwebview.utils.Utils
import org.json.JSONException
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions


/**
 * Javascript interface with few pre-made general interface function
 */
open class RapidWebViewJSInterface(
    private val context: Context,
    private val activity: Activity,
    private val webView: WebView,
) {

    /**
     * BroadCastReceiver to update push data to the page loaded on RapidWebView. The receiver
     * get certain events and performs actions based on event type. Data is pushed to website / page
     * using webView.loadUrl method.
     *
     * Events supported:
     * 1. File Upload success / failure
     * 2. Permission request callback
     *
     */
    private val broadcastReceiverForNativeCallbacks: BroadcastReceiver =
        LocalBroadCastActionUtility.createReceiver(BroadcastConstants.NATIVE_CALLBACK_ACTION) { _, intent, _ ->
            LocalBroadCastActionUtility.handleNativeCallbackAction(webView, intent)
        }

    init {
        val bManager = LocalBroadcastManager.getInstance(context)
        val intentFilter = IntentFilter()
        intentFilter.addAction(BroadcastConstants.NATIVE_CALLBACK_ACTION)
        bManager.registerReceiver(broadcastReceiverForNativeCallbacks, intentFilter)

        val downloadCompletionReceiver: BroadcastReceiver =
            LocalBroadCastActionUtility.createReceiver(DownloadManager.ACTION_DOWNLOAD_COMPLETE) { _, intent, _ ->
                LocalBroadCastActionUtility.dispatchDownloadCompletionEvent(webView, intent)
            }

        LocalBroadCastActionUtility.registerReceiver(
            context,
            downloadCompletionReceiver,
            IntentFilter().apply {
                addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            }
        )
    }


    /**
     * Start any activity within the application
     * @param destActivity: classname of activity to navigate to.
     * @param intentParams: json of parameters to be passed to activity in string format
     *
     * eg: intentParams: {"key1": 1, "key2": "value2"}
     *
     * @return Returns `true` if activity started, `false` otherwise
     */
    @JavascriptInterface
    fun startActivity(destActivity: String, intentParams: String): Boolean {
        val intent = Intent(context, Class.forName(destActivity))
        try {
            val intentData = JSONObject(intentParams)
            val intentDataKeys = intentData.keys()

            while (intentDataKeys.hasNext()) {
                val key = intentDataKeys.next()
                val value = intentData.get(key)

                when {
                    value is Int -> {
                        intent.putExtra(key, value)
                    }

                    value is String -> {
                        intent.putExtra(key, value)
                    }

                    value is Boolean -> {
                        intent.putExtra(key, value)
                    }
                }
            }
        } catch (e: JSONException) {
            return false
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        return true
    }

    /**
     * Check if permission is granted by user
     * @param permissions list of permissions to check
     * @return `true` if all permissions are granted, `false` otherwise
     */
    @JavascriptInterface
    fun checkForPermission(permissions: Array<String>): Boolean {
        return EasyPermissions.hasPermissions(context, *permissions)
    }

    /**
     * Ask for Android permission
     * @param rationaleText: Text to show on permission dialog
     * @param permissions: list of permissions to be asked to user
     * @param requestCode: Integer request-code passed for callback
     *
     * @deprecated Use {@link #requestPermissions(permissionList, callback)} instead.
     */
    @Deprecated(
        "Use requestPermissions instead",
        ReplaceWith("requestPermissions(permissionList, callback)"),
    )
    @JavascriptInterface
    fun askForPermission(rationaleText: String, permissions: Array<String>, requestCode: Int) {
        EasyPermissions.requestPermissions(
            activity,
            rationaleText,
            requestCode,
            *permissions
        )
    }

    /**
     * Get list of installed apps
     *
     * @return List of pacakge name of installed app
     */
    @JavascriptInterface
    fun getInstalledAppList(): String {
        val packageInfo = context
            .packageManager
            .getInstalledApplications(PackageManager.GET_META_DATA)
        val packages = ArrayList<String>()

        for (i in 0 until packageInfo.size) {
            packages.add(packageInfo[i].packageName)
        }
        return TextUtils.join(", ", packages);
    }

    /**
     * Vibrate device
     * @param durationMs: vibrate duration in milliseconds
     */
    @Suppress("MissingPermission")
    @JavascriptInterface
    fun vibrate(durationMs: Long = 500) {
        val vibrator = context
            .getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(
                    durationMs,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator?.vibrate(durationMs)
        }
    }

    /**
     * Copy text to clipboard
     * @param text: Text to copy to clipboard
     */
    @JavascriptInterface
    fun copyToClipboard(text: String) {
        val clipboard = context
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TEXT", text)
        clipboard.setPrimaryClip(clip)
    }

    /**
     * Show native toast message
     * @param text Text to show
     * @param duration `0` For short and `1` for long duration
     */
    @JavascriptInterface
    fun showToast(text: String, duration: Int) {
        Toast.makeText(context, text, duration).show()
    }

    /**
     * Open external browser as a new window
     *
     * @param url: Complete url to visit
     * @param packageName: Name of browser package to open (eg: "com.android.chrome")
     *
     * @return `true` if browser started, `false` otherwise
     */
    @JavascriptInterface
    fun openBrowserActivity(url: String, packageName: String): Boolean {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        browserIntent.setPackage(packageName)
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            activity.startActivity(browserIntent)
            return true
        } catch (ex: ActivityNotFoundException) {
            try {
                browserIntent.setPackage(null)
                activity.startActivity(browserIntent)
                return true
            } catch (ignored: Exception) {
            }
        }

        return false
    }

    /**
     * Open chrome custom tab
     *
     * @param url: Complete url to visit
     * @param color: Chrome custom tab color
     *
     * @return `true` if custom tab opened, `false` otherwise
     */
    @JavascriptInterface
    fun openCustomBrowserTab(url: String, color: String): Boolean {
        val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(Color.parseColor(color))

        val customTabsIntent: CustomTabsIntent = builder.build()

        try {
            customTabsIntent.launchUrl(activity, Uri.parse(url))
            return true
        } catch (ignored: Exception) {
        }

        return false
    }

    /**
     * Open a dialer (and call a number if permission is granted)
     * @param phoneNumber number to dial
     * @return `true` if call was made, `false` if dialer was opened but call not made
     */
    @JavascriptInterface
    fun openDialer(phoneNumber: String): Boolean {
        return if (!EasyPermissions.hasPermissions(context, Manifest.permission.CALL_PHONE)) {
            val uri = Uri.parse("tel:$phoneNumber")
            val callIntent = Intent(Intent.ACTION_DIAL, uri)
            callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(callIntent)
            true
        } else {
            val callText = "tel:$phoneNumber"
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse(callText)
            callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(callIntent)
            false
        }
    }

    /**
     * Open external intent
     * @param packageName Name of package to open
     * @param uri intent uri
     *
     * @return `true` if activity opened, `false` otherwise
     */
    @JavascriptInterface
    fun openExternalIntent(packageName: String, uri: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW)

        intent.setPackage(packageName)
        intent.data = Uri.parse(uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        return try {
            context.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

    /**
     * Open native share dialog
     * @param shareText the text to be shared
     *
     * @return `true` if share dialog opened, `false` otherwise
     */
    @JavascriptInterface
    fun openShareIntent(shareText: String): Boolean {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.type = "text/plain"

        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return try {
            context.startActivity(
                Intent.createChooser(sendIntent, "Share").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

    /**
     * Open native share dialog with image
     * @param shareText the text to be shared
     * @param shareImage the image file name to be shared
     *
     * Image to be shared needs to be present in cache directory before sharing.
     * Use {@link #storeInAppCache(fileUrl)} to store image in app cache.
     *
     * @return `true` if image found and share dialog opened, `false` otherwise
     */
    @JavascriptInterface
    fun openShareIntent(shareText: String, shareImage: String): Boolean {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.type = "text/plain"

        if (shareImage.isNotEmpty()) {
            val shareImageFile = RapidStorageUtility.getImageUriFromFileName(
                RapidStorageUtility.formatFileName(shareImage)
            )
            if (shareImageFile != null) {
                val photoUri =
                    FileProvider.getUriForFile(
                        context,
                        RapidStorageUtility.getAuthority(),
                        shareImageFile
                    )

                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                sendIntent.setDataAndType(
                    photoUri,
                    context.contentResolver.getType(photoUri)
                )
                sendIntent.putExtra(Intent.EXTRA_STREAM, photoUri)

                return try {
                    context.startActivity(
                        Intent.createChooser(sendIntent, "Share")
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    true
                } catch (e: ActivityNotFoundException) {
                    false
                }
            }
        }

        return false
    }

    /**
     * Share text to specific app
     * @param packageName package name of app
     * @param shareText the text to be shared
     *
     * @return `true` if share activity opened, `false` otherwise
     */
    @JavascriptInterface
    fun shareToApp(packageName: String, shareText: String): Boolean {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        shareIntent.setPackage(packageName)
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)

        return try {
            context.startActivity(shareIntent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

    /**
     * Share text with image to specific app
     * @param packageName package name of app
     * @param shareText the text to be shared
     * @param shareImage the image file name to be shared
     *
     * @return `true` if image found and share activity opened, `false` otherwise
     */
    @JavascriptInterface
    fun shareToApp(packageName: String, shareText: String, shareImage: String): Boolean {
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.setPackage(packageName)
        sendIntent.type = "text/plain"

        if (shareImage.isNotEmpty()) {
            val shareImageFile = RapidStorageUtility.getImageUriFromFileName(
                RapidStorageUtility.formatFileName(shareImage)
            )
            if (shareImageFile != null) {
                val photoUri =
                    FileProvider.getUriForFile(
                        context,
                        RapidStorageUtility.getAuthority(),
                        shareImageFile
                    )

                sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                sendIntent.setDataAndType(
                    photoUri,
                    context.contentResolver.getType(photoUri)
                )
                sendIntent.putExtra(Intent.EXTRA_STREAM, photoUri)

                return try {
                    context.startActivity(
                        Intent.createChooser(sendIntent, "Share")
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    true
                } catch (e: ActivityNotFoundException) {
                    false
                }
            }
        }
        return false
    }

    /**
     * Opens an activity to request permissions from user
     * @param permissions: List of permissions to request from user
     * @param rationaleText: Text to show on permission dialog
     */
    @JavascriptInterface
    fun requestPermissions(permissions: Array<String>, rationaleText: String) {
        val intent = Intent(activity, PermissionActivity::class.java)

        intent.putExtra(PERMISSION_LIST_KEY, permissions)
        intent.putExtra(RATIONAL_TEXT, rationaleText)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(intent)
    }

    /**
     * Open native file upload interface
     * @param fileType type of file to be uploaded. Choices: `doc`, `image`, `video`, `file`
     * @param uploadUrl PUT request Url where file will be uploaded
     * @param requestMethod: Possible Input is POST & PUT
     *
     */
    @JavascriptInterface
    fun uploadFile(fileType: String, uploadUrl: String, requestMethod: String?) {
        val intent = Intent(activity, UploadFileActivity::class.java)

        intent.putExtra("fileType", fileType)
        intent.putExtra("uploadUrl", uploadUrl)
        intent.putExtra("requestMethod", requestMethod)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(intent)
    }

    /**
     * Store a file (typically image) in local cache
     * @param fileUrl Full url of asset to download
     */
    @JavascriptInterface
    fun storeInAppCache(fileUrl: String) {
        RapidAssetCacheDownloader.DownloadTask(fileUrl).queueFileDownload()
    }

    /**
     * Check whether the file exist in cache
     * @param fileUrl the file Url to be checked in cache
     *
     * @return `true` if file exists in local cache, `false` otherwise
     */
    @JavascriptInterface
    fun checkForFileInCache(fileUrl: String): Boolean {
        return RapidStorageUtility.cacheExists(fileUrl)
    }

    /**
     * Displays a notification to the user. Requires [Manifest.permission.POST_NOTIFICATIONS] from [Build.VERSION_CODES.TIRAMISU].
     * The caller must ensure that the appropriate permissions are present before calling this method
     *
     * @param title Title of the notification.
     * @param contentText The content text of the notification.
     * @param summaryText Summary text for the notification (optional).
     * @param notificationIcon Resource name of the image used as the notification icon.
     * @param notificationImage Resource name of the image used in the expanded notification.
     * @param destActivity Name of the activity to navigate to when the notification is clicked.
     */
    @JavascriptInterface
    fun showNotification(
        title: String,
        contentText: String,
        summaryText: String?,
        notificationIcon: String,
        notificationImage: String,
        destActivity: String,
    ) {
        val mBuilder = NotificationCompat.Builder(
            context.applicationContext,
            RapidWebViewNotificationHelper.getChannelId(
                context,
                "rapidWebViewGeneral",
                "Web Notification"
            )
        )
        val intent = Intent(context.applicationContext, Class.forName(destActivity))
        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                flag
            )

        val imageNotification = getBitmapForNotification(notificationImage)
        val imageIcon = getBitmapForNotification(notificationIcon)
        if (imageNotification != null && imageIcon != null) {
            val bigPicture = NotificationCompat.BigPictureStyle()
            bigPicture.setBigContentTitle(title)
            bigPicture.setSummaryText(summaryText)
            bigPicture.bigPicture(imageNotification)
            mBuilder.setLargeIcon(imageIcon)
            mBuilder.setStyle(bigPicture)
        } else {
            val bigTextStyle = NotificationCompat.BigTextStyle()
            bigTextStyle.bigText(contentText)
            bigTextStyle.setBigContentTitle(title)
            bigTextStyle.setSummaryText(summaryText)

            mBuilder.setStyle(bigTextStyle)
        }
        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.drawable.ic_notification_bell)
        mBuilder.setContentTitle(title)
        mBuilder.setContentText(contentText)
        mBuilder.priority = NotificationCompat.PRIORITY_MAX

        mBuilder.setChannelId(
            RapidWebViewNotificationHelper.getChannelId(
                context,
                "rapidWebViewGeneral",
                "Web Notification"
            )
        )
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(0, mBuilder.build())

    }

    /**
     * returns the bitmap of the file
     * @param notificationImage name of image to be displayed in expanded notification
     */
    private fun getBitmapForNotification(notificationImage: String): Bitmap? {
        val imageFile = RapidStorageUtility.getImageUriFromFileName(
            RapidStorageUtility.formatFileName(notificationImage)
        )
        val filePath = imageFile.path
        return BitmapFactory.decodeFile(filePath)
    }

    /**
     * Close activity that holds the WebView
     */
    @JavascriptInterface
    fun closeActivity() {
        activity.finish()
    }

    /**
     * Get current Android SDK version
     */
    @JavascriptInterface
    fun getAndroidSDKVersion(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * Downloads a file from the given URL to a specified local directory.
     *
     * This method downloads a file from the provided `url` and saves it locally with the given `fileName`
     * in the specified `downloadLocation`.
     * In addition, the method fires a JavaScript event to notify the WebView of the download status.
     *
     * **Event Dispatch:**
     * - On download completion, fires a `rapid-web-view-download-listener` event with the payload:
     *     `{ detail: { eventKey: "downloadCompleted", status: "success|failure", downloadId: "downloadId" } }`
     *
     * **Note:**
     * - Requires [Manifest.permission.WRITE_EXTERNAL_STORAGE] permission on devices running Android 9 (Pie, API 28) or lower for downloading to public storage directories.
     *
     * @param url The URL of the file to be downloaded.
     *     - **Expected format:** A valid URL (e.g., https://example.com/sample.pdf).
     *
     * @param fileName The name of the file to be saved locally after download, including the file extension.
     *     - **Example:** "example-file.pdf".
     *     - **Default behavior:** If not provided, the last segment of the URL will be used as the file name.
     *     - **Note:** Ensure that the provided file name includes the appropriate MIME type (e.g., `.pdf`, `.png`, etc.).
     *
     * @param downloadLocation The location where the file will be saved on the device.
     *     - **Accepted values:**
     *         - `"PUBLIC_DOWNLOADS"`: The file will be saved in the system's public Downloads directory.
     *         - `"EXTERNAL_FILES"`: The file will be saved in the app's external files directory (specific to the app).
     *     - **Default behavior:** If not provided, `"PUBLIC_DOWNLOADS"` is chosen.
     *
     */
    @JavascriptInterface
    fun downloadFileLocally(
        url: String,
        fileName: String,
        downloadLocation: String
    ) {
        val downloadLocationEnum = Utils.toDownloadLocation(downloadLocation)
        DownLoadUtility.startDownload(context, url, fileName, downloadLocationEnum)
    }

    /**
     * Downloads a file from the given URL and opens it upon completion.
     *
     * This method downloads the file using [downloadFileLocally],
     * and automatically opens it once the download is complete. A global broadcast receiver listens for the
     * `DownloadManager.ACTION_DOWNLOAD_COMPLETE` broadcast and triggers the file opening action.
     *  In addition, the method fires a JavaScript event to notify the WebView of the download status.
     *
     * **Event Dispatch:**
     * - On download completion, fires a `rapid-web-view-download-listener` event with the payload:
     *     `{ detail: { eventKey: "downloadCompleted|downloadUnSuccessful|packageNotFound", status: "success|failure", downloadId: "downloadId" } }`
     *
     * **Note:**
     * - Requires Android 8.0 (API level 26) or higher to open the downloaded file.
     * - For Android versions before API 26, the file will be downloaded, but the automatic opening functionality will not work.
     * - This method uses a global broadcast receiver to handle the download completion action and unregisters the receiver afterward.
     * - Requires [Manifest.permission.WRITE_EXTERNAL_STORAGE] permission on devices running Android 9 (Pie, API 28) or lower for downloading to public storage directories.
     * - Requires [Manifest.permission.READ_EXTERNAL_STORAGE] permission on devices running Android 9 (Pie, API 28) or lower for opening the downloaded file.
     *
     * @param url The URL of the file to be downloaded.
     *     - **Expected format:** A valid URL (e.g., https://example.com/sample.pdf).
     *
     * @param fileName The name of the file to be saved locally after download, including the file extension.
     *     - **Example:** "example-file.pdf".
     *     - **Default behavior:** If not provided, the last segment of the URL will be used as the file name.
     *     - **Note:** Ensure that the provided file name includes the appropriate MIME type (e.g., `.pdf`, `.png`, etc.).
     *
     * @param downloadLocation The location where the file will be saved on the device.
     *     - **Accepted values:**
     *         - `"PUBLIC_DOWNLOADS"`: The file will be saved in the system's public Downloads directory.
     *         - `"EXTERNAL_FILES"`: The file will be saved in the app's external files directory (specific to the app).
     *     - **Default behavior:** If not provided, `"PUBLIC_DOWNLOADS"` is chosen.
     *
     * @see downloadFileLocally
     */
    @JavascriptInterface
    fun downloadFileLocallyAndOpen(
        url: String,
        fileName: String,
        downloadLocation: String,
    ) {
        val openFileReceiver =
            LocalBroadCastActionUtility.createReceiver(DownloadManager.ACTION_DOWNLOAD_COMPLETE) { _, intent, receiver ->
                val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
                if (downloadId != -1L) {
                    DownLoadUtility.openFileFromDownloads(context, webView, downloadId)
                }
                context.unregisterReceiver(receiver)
            }

        LocalBroadCastActionUtility.registerReceiver(context, openFileReceiver, IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        })

        downloadFileLocally(url, fileName, downloadLocation)
    }
}