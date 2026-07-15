package `in`.workindia.rapidwebview

import `in`.workindia.rapidwebview.assetcache.RapidAssetCacheClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * RapidWebViewClient overrides shouldInterceptRequest of WebViewClient
 * It's purpose is to intercept all WebView requests (including assets) -> check if the requested url
 * exists in local cache and return a copy from local cache, return from network otherwise
 *
 * Open so that consuming apps can subclass it to add their own overrides (eg: shouldOverrideUrlLoading
 * for deep-link / payment SDK handling) while still inheriting the asset-cache interception below.
 */
open class RapidWebViewClient : WebViewClient() {
    override fun shouldInterceptRequest(
        view: WebView,
        request: WebResourceRequest
    ): WebResourceResponse? {
        return RapidAssetCacheClient.onRequestCachedAsset(request.url.toString())
    }
}