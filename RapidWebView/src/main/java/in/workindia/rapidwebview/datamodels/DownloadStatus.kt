package `in`.workindia.rapidwebview.datamodels

import com.google.gson.annotations.SerializedName

data class DownloadStatus(
    @SerializedName("status")
    val status: Int,
    @SerializedName("reason")
    val reason: Int,
    @SerializedName("bytes_downloaded")
    val bytesDownloaded: Long,
    @SerializedName("total_bytes")
    val totalBytes: Long
)