package `in`.workindia.rapidwebview

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import androidx.core.content.ContextCompat

/**
 * WebChromeClient that lets trusted pages record audio with `getUserMedia`.
 *
 * The mic is granted only when both are true:
 * 1. the app already has the Android RECORD_AUDIO permission
 *    (the page can ask for it with `RapidWebViewJSInterface.requestPermissions`), and
 * 2. the page host is one of [trustedHosts] or a subdomain of one.
 *
 * Every other request is denied. The class is open so apps can keep their own
 * callbacks (eg: `onProgressChanged`) by extending it.
 *
 * @param context used to check the RECORD_AUDIO permission
 * @param trustedHosts hosts allowed to use the mic, eg: listOf("example.com")
 */
open class RapidWebChromeClient(
    private val context: Context,
    private val trustedHosts: List<String>,
) : WebChromeClient() {

    override fun onPermissionRequest(request: PermissionRequest) {
        val wantsAudio = request.resources.contains(PermissionRequest.RESOURCE_AUDIO_CAPTURE)
        val hasMicPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO,
        ) == PackageManager.PERMISSION_GRANTED

        if (wantsAudio && hasMicPermission && isTrustedHost(request.origin?.host)) {
            request.grant(arrayOf(PermissionRequest.RESOURCE_AUDIO_CAPTURE))
        } else {
            request.deny()
        }
    }

    /**
     * @return `true` if [host] is a trusted host or a subdomain of one
     */
    protected open fun isTrustedHost(host: String?): Boolean {
        if (host.isNullOrEmpty()) return false
        return trustedHosts.any { host == it || host.endsWith(".$it") }
    }
}
