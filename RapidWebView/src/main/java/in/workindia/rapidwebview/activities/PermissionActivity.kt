package `in`.workindia.rapidwebview.activities

import `in`.workindia.rapidwebview.R
import `in`.workindia.rapidwebview.constants.BroadcastConstants
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.requestPermissions


class PermissionActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private val rcGenericPermission: Int = 12312
    private lateinit var permissions: Array<String>
    private var callback: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        val intent = intent

        permissions = intent?.getStringArrayExtra("permissionList") as Array<String>
        callback = intent?.getStringExtra("callback")
        val rationalText = intent?.getStringExtra("rationalText") ?: "This permission is required"

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
        intentBroadcast.putExtra(BroadcastConstants.CALLBACK, callback)
        intentBroadcast.putExtra(BroadcastConstants.PERMISSION_LIST, permissions)

        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intentBroadcast)

        finish()
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