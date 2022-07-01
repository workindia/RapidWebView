package `in`.workindia.rapidwebview.activities.service

import `in`.workindia.rapidwebview.RapidWebViewNotificationHelper
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import `in`.workindia.rapidwebview.network.RetrofitHelper
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileInputStream


class UploadService : Service() {

    var fileUri: String = ""
    var uploadUrl: String = ""
    var requestMethod: String = ""
    var fileName: String = ""

    private var uploadStatusMutableLiveData: MutableLiveData<String> = MutableLiveData()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fileUri = intent?.getStringExtra("fileUri") ?: ""
        uploadUrl = intent?.getStringExtra("uploadUrl") ?: ""
        requestMethod = intent?.getStringExtra("requestMethod") ?: "POST"
        fileName = intent?.getStringExtra("fileName") ?: ""

        if (fileUri.isBlank() || uploadUrl.isBlank()) {
            return START_NOT_STICKY
        }

        createNotification(
            "Upload",
            "Uploading your files",
            android.R.drawable.stat_sys_upload,
            true
        )

        uploadFilesUsingService()

        uploadStatusMutableLiveData.observeForever {
            val intentBroadcast = Intent(BroadcastConstants.NATIVE_CALLBACK_ACTION)
            if (it.equals("success")) {
                intentBroadcast.putExtra(BroadcastConstants.UPLOAD, "success")
                intentBroadcast.putExtra(BroadcastConstants.UPLOADED_FILE_NAME, fileName)
                intentBroadcast.putExtra(BroadcastConstants.UPLOADED_URL, uploadUrl)

                createNotification(
                    "Success",
                    "Successfully uploaded your files",
                    android.R.drawable.stat_sys_upload_done,
                    false
                )
            } else {
                intentBroadcast.putExtra(BroadcastConstants.UPLOAD, "failure")
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentBroadcast)

            stopSelf()
        }
        return START_NOT_STICKY
    }

    private fun createNotification(
        title: String,
        contentText: String,
        drawable: Int,
        onGoing: Boolean
    ) {
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            FLAG_UPDATE_CURRENT
        )

        val notification =
            NotificationCompat.Builder(
                this,
                RapidWebViewNotificationHelper.getChannelId(this, "rapidWebViewUpload", "Upload")
            )
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(drawable)
                .setContentIntent(pendingIntent)
                .setOngoing(onGoing)
                .build()

        startForeground(90, notification)
    }

    private fun uploadFilesUsingService() {
        val uri = Uri.parse(fileUri)
        val fis = FileInputStream(
            this.contentResolver?.openFileDescriptor(
                uri,
                "r"
            )?.fileDescriptor
        )
        val byteArray = fis?.readBytes()

        val mediaType: MediaType? = contentResolver.getType(Uri.parse(fileUri))?.toMediaTypeOrNull()

        if (byteArray != null && mediaType != null) {
            val requestBody: RequestBody = byteArray.toRequestBody(mediaType)
            if (requestBody.contentLength() > 0) {
                uploadFile(uploadUrl, requestBody, uploadStatusMutableLiveData, requestMethod)
                return
            }
        }
        uploadStatusMutableLiveData.postValue("failure")
    }


    private fun uploadFile(
        signedUrl: String,
        requestBody: RequestBody,
        uploadStatusMutableLiveData: MutableLiveData<String>,
        requestMethod: String
    ) {
        var call: Call<Void>? = null
        if (requestMethod == "POST")
            call = RetrofitHelper.createService.uploadFileViaPost(signedUrl, requestBody)
        else if (requestMethod == "PUT")
            call = RetrofitHelper.createService.uploadFileViaPut(signedUrl, requestBody)

        if (call == null) {
            uploadStatusMutableLiveData.postValue("failure")
            return
        }
        call.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    uploadStatusMutableLiveData.postValue("success")
                } else {
                    uploadStatusMutableLiveData.postValue("failure")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                uploadStatusMutableLiveData.postValue("failure")
            }
        })
    }
}