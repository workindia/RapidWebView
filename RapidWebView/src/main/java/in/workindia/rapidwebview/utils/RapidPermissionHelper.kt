package `in`.workindia.rapidwebview.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi

object RapidPermissionHelper {

    /**
     * Get list of permissions required for read and write
     * Since [Build.VERSION_CODES.TIRAMISU] need [Manifest.permission.READ_MEDIA_IMAGES], [Manifest.permission.READ_MEDIA_VIDEO], [Manifest.permission.READ_MEDIA_AUDIO]
     * Below [Build.VERSION_CODES.TIRAMISU] need [Manifest.permission.WRITE_EXTERNAL_STORAGE], [Manifest.permission.READ_EXTERNAL_STORAGE]
     */
    fun requiredPermissionsForReadAndWrite(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    /**
     * Get list of permissions required for uploading files
     * Since [Build.VERSION_CODES.TIRAMISU] need [Manifest.permission.READ_MEDIA_IMAGES], [Manifest.permission.READ_MEDIA_VIDEO], [Manifest.permission.READ_MEDIA_AUDIO]
     * Below [Build.VERSION_CODES.TIRAMISU] need [Manifest.permission.READ_EXTERNAL_STORAGE]
     */
    fun requiredPermissionsForRead(fileType: String): Array<String> {
        val requiredPermissions = when (fileType) {
            "image" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            "video" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf(Manifest.permission.READ_MEDIA_VIDEO)
                } else {
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            "doc", "files" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf()
                } else {
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arrayOf()
                } else {
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }

        return requiredPermissions
    }

    /**
     * register receiver for download complete action i.e [DownloadManager.ACTION_DOWNLOAD_COMPLETE] for API 34
     *
     * Since [Build.VERSION_CODES.UPSIDE_DOWN_CAKE] Programmatically registered BrodCastReceivers using `` context.registerReceiver() ``call must specify flag [RECEIVER_EXPORTED] | [RECEIVER_NOT_EXPORTED]
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun registerDownLoadCompleteApi34(context: Context, broadcastReceiver: BroadcastReceiver) {
        val intentFilter = IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        }

        context.registerReceiver(
            broadcastReceiver,
            intentFilter,
            RECEIVER_EXPORTED
        )
    }

    /**
     * register receiver for download complete action i.e [DownloadManager.ACTION_DOWNLOAD_COMPLETE] for API 33 and below
     *
     * call `` context.registerReceiver() `` required minimum version [Build.VERSION_CODES.O]
     *
     * @see registerDownLoadCompleteApi34
     */
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @RequiresApi(Build.VERSION_CODES.O)
    fun registerDownLoadCompleteBase(context: Context, broadcastReceiver: BroadcastReceiver) {
        val intentFilter = IntentFilter().apply {
            addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        }

        context.registerReceiver(
            broadcastReceiver,
            intentFilter
        )
    }
}