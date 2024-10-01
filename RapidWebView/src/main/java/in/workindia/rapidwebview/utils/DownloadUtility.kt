package `in`.workindia.rapidwebview.utils

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.webkit.WebView
import androidx.core.content.FileProvider
import `in`.workindia.rapidwebview.R
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.EVENT_DOWNLOAD_LISTENER
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.EVENT_KEY
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.FAILURE
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.KEY_DETAIL
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.KEY_DOWNLOAD_UNSUCCESSFUL
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.KEY_PACKAGE_NOT_FOUND
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.PROVIDER_SUFFIX
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.STATUS
import `in`.workindia.rapidwebview.datamodels.DownloadLocation
import java.io.File

object DownloadUtility {

    /**
     * Downloads a file from the given URL to a specified local directory.
     *
     * @param url The URL of the file to be downloaded.
     * @param context The context, used to access system services.
     * @param fileName The name of the file to be saved locally after download, including the file extension. Default value: Last segment of the URL
     * @param downloadLocation The location where the file will be saved. Defaults value: [DownloadLocation.PUBLIC_DOWNLOADS].
     * @return `true` if the download was successfully initiated, `false` otherwise.
     */
    fun startDownload(
        context: Context,
        url: String,
        fileName: String? = null,
        downloadLocation: DownloadLocation = DownloadLocation.PUBLIC_DOWNLOADS,
    ): Boolean {
        try {
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri)
            val validFileName = Utils.getFileNameFromUri(fileName, uri)

            request.setTitle(validFileName)
            request.setDescription(context.resources.getString(R.string.downloading_file))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

            setDownloadDestination(context, request, downloadLocation, validFileName)

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
            return true
        } catch (exception: Exception) {
            return false
        }
    }

    /**
     * Sets the destination for the download request on the specified download location.
     *
     * @param downloadLocation The location where the file will be saved.
     * @param fileName The valid filename to be used for saving the file.
     * @param context The context, used to access system services.
     * @param request The download request to which the destination is being set.
     */
    private inline fun setDownloadDestination(
        context: Context,
        request: DownloadManager.Request,
        downloadLocation: DownloadLocation,
        fileName: String,
    ) {
        when (downloadLocation) {
            DownloadLocation.EXTERNAL_FILES -> {
                request.setDestinationInExternalFilesDir(
                    context,
                    Environment.DIRECTORY_DOWNLOADS, fileName
                )
            }

            DownloadLocation.PUBLIC_DOWNLOADS -> {
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    fileName
                )
            }
        }
    }


    /**
     * Opens a downloaded file using the system's default viewer based on the file's MIME type.
     *
     * @param context The context used to access system services and start activities.
     * @param webView The WebView used to dispatch appropriate events.
     * @param downloadId The ID of the download to be opened, obtained from the download manager.
     */
    fun openFileFromDownloads(context: Context, webView: WebView, downloadId: Long) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(downloadId)

        downloadManager.query(query).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                val mimeTypeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE)

                // Check if column indices are valid
                if (statusIndex >= 0 && uriIndex >= 0 && mimeTypeIndex >= 0) {
                    val downloadStatus = cursor.getInt(statusIndex)
                    val downloadLocalUri = cursor.getString(uriIndex)
                    val downloadMimeType = cursor.getString(mimeTypeIndex)

                    if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL && !downloadLocalUri.isNullOrBlank()) {
                        openFile(
                            context,
                            webView,
                            Uri.parse(downloadLocalUri),
                            downloadMimeType
                        )
                    } else {
                        val downloadUnSuccessfulEvent = LocalBroadcastActionUtility.generateJavaScriptEvent(
                            EVENT_DOWNLOAD_LISTENER,
                            mapOf(
                                EVENT_KEY to KEY_DOWNLOAD_UNSUCCESSFUL,
                                STATUS to FAILURE,
                                KEY_DETAIL to "Download with downloadId $downloadId was unsuccessful"
                            )
                        )
                        LocalBroadcastActionUtility.dispatchJavaScript(downloadUnSuccessfulEvent, webView)
                    }
                }
            }
        }
    }

    /**
     * Opens a file attachment using an appropriate app based on its MIME type.
     *
     * @param context The context used to start the activity for viewing the file.
     * @param webView The WebView used to dispatch appropriate events.
     * @param attachmentUri The URI of the file to be opened.
     * @param attachmentMimeType The MIME type of the downloaded file.
     */
    private fun openFile(
        context: Context,
        webView: WebView,
        attachmentUri: Uri,
        attachmentMimeType: String,
    ) {
        val uri = if (ContentResolver.SCHEME_FILE == attachmentUri.scheme) {
            val file = File(attachmentUri.path ?: return)
            FileProvider.getUriForFile(
                context,
                "${context.applicationContext.packageName}${PROVIDER_SUFFIX}",
                file
            )
        } else {
            attachmentUri
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, attachmentMimeType)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val activityNotFoundEvent = LocalBroadcastActionUtility.generateJavaScriptEvent(
                EVENT_DOWNLOAD_LISTENER,
                mapOf(
                    EVENT_KEY to KEY_PACKAGE_NOT_FOUND,
                    STATUS to FAILURE,
                    KEY_DETAIL to "Package not found for opening file type $attachmentMimeType"
                )
            )
            LocalBroadcastActionUtility.dispatchJavaScript(activityNotFoundEvent, webView)
        }
    }
}