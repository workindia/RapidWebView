package `in`.workindia.rapidwebview.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import `in`.workindia.rapidwebview.R
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.DOWNLOAD_LOCATION
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.DOWNLOAD_URL
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.FILE_NAME
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.PERMISSION_LIST_INTENT
import `in`.workindia.rapidwebview.constants.BroadcastConstants.Companion.RATIONAL_TEXT
import `in`.workindia.rapidwebview.utils.Utils
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.requestPermissions


class PermissionActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val rcGenericPermission: Int = 12312
    private lateinit var permissions: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        val intent = intent

        permissions = intent?.getStringArrayExtra(PERMISSION_LIST_INTENT) as Array<String>
        val rationalText = intent?.getStringExtra(RATIONAL_TEXT) ?: "This permission is required"

        if (permissions.isEmpty()) {
            throw IllegalArgumentException("PermissionActivity requires `permissionList` intent data")
        }

        if (EasyPermissions.hasPermissions(this, *permissions)) {
            onPermissionCallback("success", permissions)
        } else {
            requestPermissions(
                this,
                rationalText,
                rcGenericPermission,
                *permissions
            )
        }
    }

    private fun onPermissionCallback(status: String, permissions: Array<String>) {
        val intentBroadcast = Intent(BroadcastConstants.NATIVE_CALLBACK_ACTION)

        intentBroadcast.putExtra(BroadcastConstants.PERMISSION, status)
        intentBroadcast.putExtra(BroadcastConstants.PERMISSION_LIST, permissions)
        appendPendingDataToIntent(intentBroadcast)

        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentBroadcast)

        finish()
    }

    private fun appendPendingDataToIntent(intentBrodCast: Intent) {
        val bundle = intent.extras
        val url: String? = bundle?.getString(DOWNLOAD_URL)
        val fileName: String? = bundle?.getString(FILE_NAME)
        val downloadLocationName: String =
            Utils.toDownloadLocation(bundle?.getString(DOWNLOAD_LOCATION)).name

        intentBrodCast.putExtras(Bundle().apply {
            putString(DOWNLOAD_URL, url)
            putString(FILE_NAME, fileName)
            putString(DOWNLOAD_LOCATION, downloadLocationName)
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == rcGenericPermission) {
            onPermissionCallback("success", perms.toTypedArray())
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == rcGenericPermission) {
            onPermissionCallback("failure", perms.toTypedArray())
        }
    }
}