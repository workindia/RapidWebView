package `in`.workindia.rapidwebview.utils

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import `in`.workindia.rapidwebview.datamodels.DownloadLocation
import `in`.workindia.rapidwebview.R
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.PROVIDER_SUFFIX
import `in`.workindia.rapidwebview.utils.Utils.validateUrl
import java.io.File

object RapidDownLoadUtility {

    /**
     * Starts a download using the system's `DownloadManager`.
     *
     * @param url The URL from which the file is to be downloaded. Must be a valid URL.
     * @param context The context, used to access system services.
     * @param fileName The name of the file to be downloaded. If null, the name will be extracted from the URL. Defaults to null.
     * @param downloadLocation The location where the file will be saved. Defaults to [DownloadLocation.PUBLIC_DOWNLOADS].
     * @return `true` if the download was successfully initiated, `false` otherwise.
     */
    fun startDownload(
        url: String?,
        context: Context,
        fileName: String? = null,
        downloadLocation: DownloadLocation = DownloadLocation.PUBLIC_DOWNLOADS,
    ): Boolean {
        validateUrl(url) {
            return false
        }

        try {
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri)
            val validFileName = Utils.getFileNameFromUri(fileName, uri)

            request.setTitle(validFileName)
            request.setDescription(context.resources.getString(R.string.downloading_file))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

            setDownloadDestination(downloadLocation, validFileName, context, request)

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
            return true
        } catch (exception: Exception) {
            return false
        }
    }

    /**
     * Sets the destination for the download based on the specified download location.
     *
     * @param downloadLocation The location where the file will be saved.
     * @param validFileName The valid filename to be used for saving the file.
     * @param context The context, used to access system services.
     * @param request The download request to which the destination is being set.
     */
    private fun setDownloadDestination(
        downloadLocation: DownloadLocation,
        validFileName: String,
        context: Context,
        request: DownloadManager.Request,
    ) {
        when (downloadLocation) {
            DownloadLocation.EXTERNAL_FILES -> {
                request.setDestinationInExternalFilesDir(
                    context,
                    Environment.DIRECTORY_DOWNLOADS, validFileName
                )
            }

            DownloadLocation.PUBLIC_DOWNLOADS -> {
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    validFileName
                )
            }
        }
    }


    /**
     * Opens a downloaded file using the system's default viewer based on the file's MIME type.
     *
     * @param context The context used to access system services and start activities.
     * @param downloadId The ID of the download to be opened, obtained from the download manager.
     */
    fun openDownLoadedFile(context: Context, downloadId: Long) {
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
                        openDownloadedAttachment(
                            context,
                            Uri.parse(downloadLocalUri),
                            downloadMimeType
                        )
                    }
                }
            }
        }
    }

    /**
     * Opens a downloaded file attachment using an appropriate app based on its MIME type.
     *
     * @param context The context used to start the activity for viewing the file.
     * @param attachmentUri The URI of the downloaded file to be opened.
     * @param attachmentMimeType The MIME type of the downloaded file.
     */
    private fun openDownloadedAttachment(
        context: Context,
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
            Toast.makeText(context, context.resources.getString(R.string.cannot_open_file), Toast.LENGTH_SHORT).show()
        }
    }
}