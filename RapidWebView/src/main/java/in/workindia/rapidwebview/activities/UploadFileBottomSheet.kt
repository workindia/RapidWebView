package `in`.workindia.rapidwebview.activities

import `in`.workindia.rapidwebview.R
import `in`.workindia.rapidwebview.activities.service.UploadService
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import android.Manifest
import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.Group
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import br.com.onimur.handlepathoz.HandlePathOz
import br.com.onimur.handlepathoz.HandlePathOzListener
import br.com.onimur.handlepathoz.model.PathOz
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


class UploadFileBottomSheet : BottomSheetDialogFragment(), HandlePathOzListener.SingleUri,
    EasyPermissions.PermissionCallbacks {

    private var fileType: String = ""
    private var uploadUrl: String = ""
    private var callback: String? = null
    private var requestMethod: String? = null
    private var fileUri: String? = null

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var handlePathOz: HandlePathOz
    private lateinit var bottomSheetDialogInterface: BottomSheetDialogInterface
    private lateinit var pathOz: PathOz


    companion object {
        private const val rcPermissionDocument: Int = 2121

        @JvmStatic
        fun getInstance(
            fileType: String,
            uploadUrl: String,
            callback: String?,
            requestMethod: String?,
            bottomSheetDialogInterface: BottomSheetDialogInterface
        ): UploadFileBottomSheet {
            val uploadResumeChatBottomSheet = UploadFileBottomSheet()

            uploadResumeChatBottomSheet.fileType = fileType
            uploadResumeChatBottomSheet.uploadUrl = uploadUrl
            uploadResumeChatBottomSheet.callback = callback
            uploadResumeChatBottomSheet.requestMethod = requestMethod
            uploadResumeChatBottomSheet.bottomSheetDialogInterface = bottomSheetDialogInterface

            return uploadResumeChatBottomSheet
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        handlePathOz = HandlePathOz(requireActivity(), this)
        return inflater.inflate(R.layout.bottomsheet_upload_file, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri: Uri? = result.data?.data
                    fileUri = uri.toString()
                    uri?.let { handlePathOz.getRealPath(it) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_choose_file).setOnClickListener {
            chooseUpload()
        }
        view.findViewById<Group>(R.id.file_present).visibility = View.GONE
        view.findViewById<Group>(R.id.file_absent).visibility = View.VISIBLE

        view.findViewById<Button>(R.id.btn_send_file)?.setOnClickListener {

            val serviceIntent = Intent(activity, UploadService::class.java)
            serviceIntent.putExtra("uploadUrl", uploadUrl)
            serviceIntent.putExtra("fileUri", fileUri)
            serviceIntent.putExtra("callback", callback)
            serviceIntent.putExtra("requestMethod", requestMethod)
            activity?.startService(serviceIntent)

        }
    }

    @AfterPermissionGranted(rcPermissionDocument)
    private fun chooseUpload() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            when (fileType) {
                "doc" -> {
                    uploadDocFile()
                }
                "image" -> {
                    uploadImageFile()
                }
                "video" -> {
                    uploadVideoFile()
                }
                "files" -> {
                    uploadGeneralFile()
                }
            }
        } else {
            EasyPermissions.requestPermissions(
                this,
                "File Storage Permission is Required",
                rcPermissionDocument,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun uploadDocFile() {
        val mimeTypes = arrayOf(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
            "application/vnd.ms-word.document.macroEnabled.12",
            "application/vnd.ms-word.template.macroEnabled.12",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/vnd.openxmlformats-officedocument.presentationml.template",
            "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/vnd.ms-excel.sheet.macroEnabled.12",
            "application/vnd.ms-excel.template.macroEnabled.12",
            "application/vnd.ms-excel.addin.macroEnabled.12",
            "application/vnd.ms-excel.sheet.binary.macroEnabled.12",
            "application/vnd.ms-powerpoint",
            "application/vnd.ms-powerpoint.addin.macroEnabled.12",
            "application/vnd.ms-powerpoint.presentation.macroEnabled.12",
            "application/vnd.ms-powerpoint.template.macroEnabled.12",
            "application/vnd.ms-powerpoint.slideshow.macroEnabled.12",
            "text/plain",
            "application/pdf"
        )

        val galleryIntent = Intent(Intent.ACTION_OPEN_DOCUMENT, null)
        galleryIntent.type = "*/*"
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
        galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        activityResultLauncher.launch(galleryIntent)
    }

    private fun uploadImageFile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Image"))
    }

    private fun uploadVideoFile() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Video"))
    }

    private fun uploadGeneralFile() {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityResultLauncher.launch(Intent.createChooser(intent, "Select File"))
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == rcPermissionDocument) {

            val builder = AppSettingsDialog.Builder(this)
            builder.setTitle("Permission Required")
            builder.setRationale("File Storage Permission is Required to Upload Files")

            builder.build().show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onRequestHandlePathOz(pathOz: PathOz, tr: Throwable?) {
        if (!pathOz.path?.equals("")) {
            view?.findViewById<Group>(R.id.file_present)?.visibility = View.VISIBLE
            view?.findViewById<Group>(R.id.file_absent)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.tv_file_name)?.text = File(pathOz?.path)?.name ?: ""
            this.pathOz = pathOz
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        bottomSheetDialogInterface?.onBottomSheetClose(dialog)
    }

    private fun registerReceiver() {
        val bManager = LocalBroadcastManager.getInstance(requireContext())
        val intentFilter = IntentFilter()
        intentFilter.addAction(BroadcastConstants.NATIVE_CALLBACK_ACTION)
        bManager.registerReceiver(broadcastReceiver, intentFilter)
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == BroadcastConstants.NATIVE_CALLBACK_ACTION && intent.hasExtra(
                    BroadcastConstants.UPLOAD
                )
            ) {
                dismiss()
            }
        }
    }

}