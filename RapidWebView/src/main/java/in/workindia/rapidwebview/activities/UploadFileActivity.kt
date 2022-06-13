package `in`.workindia.rapidwebview.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class UploadFileActivity : AppCompatActivity(), BottomSheetDialogInterface {
    private var fileType: String? = ""
    private var uploadUrl: String? = ""
    private var callback: String? = null
    private var requestMethod: String? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        val intent = intent

        fileType = intent?.getStringExtra("fileType")
        uploadUrl = intent?.getStringExtra("uploadUrl")
        callback = intent?.getStringExtra("callback")
        requestMethod = intent?.getStringExtra("requestMethod")

        if (fileType.isNullOrBlank() || uploadUrl.isNullOrBlank()) {
            throw IllegalArgumentException("UploadFileActivity requires `fileType` and `uploadUrl` intent data")
        }

        val uploadFileBottomSheet =
            UploadFileBottomSheet.getInstance(
                fileType ?: "",
                uploadUrl ?: "",
                callback,
                requestMethod ?: "POST",
                this
            )

        uploadFileBottomSheet.show(supportFragmentManager, "")
    }

    override fun onBottomSheetClose(dialog: DialogInterface) {
        finish()
    }
}