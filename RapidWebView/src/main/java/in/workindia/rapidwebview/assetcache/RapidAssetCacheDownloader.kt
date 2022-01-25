package `in`.workindia.rapidwebview.assetcache

import `in`.workindia.rapidwebview.RapidClient
import `in`.workindia.rapidwebview.datamodels.AssetManifest
import `in`.workindia.rapidwebview.network.RetrofitHelper
import android.util.Log
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

/**
 * RapidAssetCacheDownloader - This utility caches the assets provided by asset-manifest on
 * local disk and maintains it
 */
class RapidAssetCacheDownloader {

    companion object {
        const val TAG = "RapidAssetCacheDownload"

        /**
         * Begin the process of caching assets defined by asset provided by `url` param
         * @param url: Url pointing to asset-manifest.json file
         */
        @JvmStatic
        fun initialise(url: String) {
            fetchAssetManifest(url, object : AssetManifestDownloadCallback {
                override fun callback(assetManifest: AssetManifest) {
                    if (!RapidAssetCacheClient.compareAssetVersion(assetManifest.version)) {
                        beginCachingTask(
                            assetManifest.assetUrls.toCollection(ArrayList()),
                            assetManifest.version
                        )
                    } else {
                        Log.d(TAG, "RapidAssetCacheDownloader :: Latest Assets found on disk")
                        RapidAssetCacheClient.onAssetsRestore()
                    }
                }
            })
        }

        /**
         * Fetch latest asset-manifest from network
         * @param url: Url pointing to asset-manifest.json file
         * @param assetManifestDownloadCallback: Interface which runs a callback when asset fetch is completed
         */
        private fun fetchAssetManifest(
            url: String,
            assetManifestDownloadCallback: AssetManifestDownloadCallback
        ) {
            val restApi = RetrofitHelper.createService
            restApi.getAsset(url).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val assetManifest =
                            Gson().fromJson(response.body()!!.string(), AssetManifest::class.java)
                        assetManifestDownloadCallback.callback(assetManifest)
                        Log.i(
                            TAG,
                            "RapidAssetCacheDownloader.fetchAssetManifest :: Asset Manifest Downloaded: $url"
                        )
                    } else {
                        Log.e(
                            TAG,
                            "RapidAssetCacheDownloader.fetchAssetManifest :: Failed to load asset json form url: $url"
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(
                        TAG,
                        "RapidAssetCacheDownloader.fetchAssetManifest :: Failed to load asset json form url: $url"
                    )
                }
            })
        }

        private fun beginCachingTask(assetList: ArrayList<String>, version: String) {
            RapidAssetCacheClient.onAssetsDownloadTaskInitiated(assetList)

            val timer = Timer()
            val timerTask: TimerTask = object : TimerTask() {
                override fun run() {
                    if (!RapidAssetCacheClient.isAssetDownloadTaskCompleted()) {
                        RapidAssetCacheClient.getPendingAssetToBeCached(assetList).forEach { url ->
                            // TODO: Add checksum check
                            DownloadTask(url).queueFileDownload()
                        }
                    } else {
                        RapidAssetCacheClient.onAssetsDownloadTaskCompleted(version)
                        timer.cancel()
                    }
                }
            }

            timer.schedule(timerTask, 0, 10000)
        }
    }

    /**
     * This interface is called when asset manifest json file is fetched and processed
     * as AssetManifest object
     */
    private interface AssetManifestDownloadCallback {
        /**
         * Callback when manifest json is fetched
         * @param assetManifest: AssetManifest object
         */
        fun callback(assetManifest: AssetManifest)
    }

    /**
     * DownloadTask queues asset to be downloaded and marks their status
     * @param url: Asset URL to download
     */
    class DownloadTask constructor(val url: String) {
        fun queueFileDownload() {
            RapidAssetCacheClient.onAssetDownloadInitiated(url)

            val restApi = RetrofitHelper.createService
            restApi.getAsset(url).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseUrl = response.raw().request.url.toString()
                        val responseBody = (response.body() ?: "") as ResponseBody

                        RapidAssetCacheClient.onAssetDownloadComplete(responseUrl, responseBody)
                        Log.i(
                            TAG,
                            "DownloadTask.queueFileDownload :: Asset Downloaded: $responseUrl"
                        )
                    } else {
                        val responseUrl = response.raw().request.url.toString()

                        RapidAssetCacheClient.onAssetDownloadFailure(responseUrl)
                        Log.e(
                            TAG,
                            "DownloadTask.queueFileDownload :: Failed to download asset: $responseUrl"
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    val responseUrl = call.request().url.toString()

                    RapidAssetCacheClient.onAssetDownloadFailure(responseUrl)
                    Log.e(
                        TAG,
                        "DownloadTask.queueFileDownload :: Failed to download asset: $responseUrl"
                    )
                }
            })
        }
    }

}