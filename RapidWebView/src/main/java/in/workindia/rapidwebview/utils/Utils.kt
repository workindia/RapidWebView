package `in`.workindia.rapidwebview.utils

import android.net.Uri
import `in`.workindia.rapidwebview.datamodels.DownloadLocation

object Utils {

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
    fun toDownloadLocation(location: String): DownloadLocation {
        return try {
            DownloadLocation.valueOf(location)
        } catch (e: IllegalArgumentException) {
            DownloadLocation.PUBLIC_DOWNLOADS
        }
    }
}