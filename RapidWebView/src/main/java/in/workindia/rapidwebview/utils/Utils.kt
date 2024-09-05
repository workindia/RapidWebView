package `in`.workindia.rapidwebview.utils

import android.net.Uri
import `in`.workindia.rapidwebview.datamodels.DownloadLocation

object Utils {

    /**
     * Check url is a valid string or not and run [block] if not
     */
    inline fun validateUrl(url: String?, block: () -> Unit) {
        if (url.isNullOrBlank()) {
            block()
        }
    }

    /**
     *  @return fileName if valid String otherwise return lastPathSegment of uri
     */
    fun getFileNameFromUri(fileName: String?, uri: Uri): String {
        return if (fileName.isNullOrBlank()) {
            uri.lastPathSegment ?: "file"
        } else {
            fileName
        }
    }

    /**
     *  @return [DownloadLocation] returns matching value from [DownloadLocation] enum.
     *  Default value is [DownloadLocation.PUBLIC_DOWNLOADS]
     */
    fun toDownloadLocation(location: String?): DownloadLocation {
        return try {
            if (location != null) {
                DownloadLocation.valueOf(location)
            } else {
                DownloadLocation.PUBLIC_DOWNLOADS
            }
        } catch (e: IllegalArgumentException) {
            DownloadLocation.PUBLIC_DOWNLOADS
        }
    }
}