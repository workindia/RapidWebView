package `in`.workindia.rapidwebview.assetcache

import android.webkit.WebResourceResponse
import okhttp3.ResponseBody

/**
 * RapidAssetCacheClient - Interacts with asset-cache stored on disk
 * Reads and manages asset data holder singleton
 */
class RapidAssetCacheClient {

    companion object {
        /**
         * State: Download has failed
         */
        const val STATUS_FAILED = -1

        /**
         * State: Download is initiated but not completed
         */
        const val STATUS_PENDING = 0

        /**
         * State: Download is successful but not written to the disk
         */
        const val STATUS_SUCCESS = 1

        /**
         * State: Download is successful and written to the disk
         */
        const val STATUS_WRITTEN_TO_DISK = 2

        /**
         * When asset download task is initiated, this method is invoked
         */
        @JvmStatic
        fun onAssetsDownloadTaskInitiated(assetList: ArrayList<String>) {
            RapidAssetCacheObject.clearAssetMap()
            RapidAssetCacheObject.setAssetDownloadStatus(STATUS_PENDING)
            RapidStorageUtility.clearCachedAssets()

            assetList.forEach { asset -> RapidAssetCacheObject.setAssetUrl(asset, STATUS_PENDING) }
        }

        /**
         * When asset download task is completed, this method is invoked
         * @param version: Version of assets downloaded
         */
        @JvmStatic
        fun onAssetsDownloadTaskCompleted(version: Int) {
            RapidAssetCacheObject.setAssetDownloadStatus(STATUS_SUCCESS)
            RapidAssetCacheObject.setAssetVersion(version)
        }

        /**
         * When asset version from network and local is same and we want to load them locally
         */
        @JvmStatic
        fun onAssetsRestore() {
            val listCachedFile = RapidStorageUtility.listCachedFiles()
            listCachedFile?.forEach { file ->
                RapidAssetCacheObject.setAssetUrl(file.name, STATUS_WRITTEN_TO_DISK)
            }
        }

        /**
         * When an individual asset download is initiated, this method is invoked
         * @param url: Full url of asset
         */
        @JvmStatic
        fun onAssetDownloadInitiated(url: String) {
            RapidAssetCacheObject.setAssetUrl(url, STATUS_PENDING)
        }

        /**
         * When an individual asset download is completed, this method is invoked
         * @param url: Full url of asset
         * @param assetBody: Content body received from network
         */
        @JvmStatic
        fun onAssetDownloadComplete(url: String, assetBody: ResponseBody) {
            RapidAssetCacheObject.setAssetUrl(url, STATUS_SUCCESS)
            writeAssetToDisk(url, assetBody)
        }

        /**
         * When an individual asset download is completed and written to disk,
         * this method is invoked
         * @param url: Full url of asset
         */
        @JvmStatic
        fun onAssetDownloadWrittenToDisk(url: String) {
            RapidAssetCacheObject.setAssetUrl(url, STATUS_WRITTEN_TO_DISK)
        }

        /**
         * When an individual asset download is faled, this method is invoked
         * @param url: Full url of asset
         */
        @JvmStatic
        fun onAssetDownloadFailure(url: String) {
            RapidAssetCacheObject.setAssetUrl(url, STATUS_FAILED)
        }

        @JvmStatic
        fun onRequestCachedAsset(url: String): WebResourceResponse? {
            return if (RapidStorageUtility.cacheExists(url)) {
                val mimeType = RapidStorageUtility.getAssetMimeType(url)
                val cachedFileInputStream = RapidStorageUtility.getCachedAsset(url)

                if (mimeType != "") {
                    WebResourceResponse(mimeType, "UTF-8", cachedFileInputStream)
                } else {
                    null
                }
            } else {
                null
            }
        }

        /**
         * Check if Asset Download Task is completed. If download is incomplete, a download retry
         * is done every 10 seconds via timeTask in RapidAssetCacheDownloader
         * @return `true` if all assets are successfully downloaded, `false` otherwise
         */
        @JvmStatic
        fun isAssetDownloadTaskCompleted(): Boolean {
            if (RapidAssetCacheObject.getAssetDownloadStatus()) {
                return true
            }

            for ((_, status) in RapidAssetCacheObject.getAssetMap()) {
                if (status != STATUS_SUCCESS && status != STATUS_WRITTEN_TO_DISK) {
                    return false
                }
            }

            return true
        }

        /**
         * When an individual asset download is completed, this method is invoked
         * @param assetList: Complete list of assets fetched from asset-manifest json
         * @return list of pending assets to be downloaded completely
         */
        @JvmStatic
        fun getPendingAssetToBeCached(assetList: ArrayList<String>): List<String> {
            val localAssetList = RapidStorageUtility.listCachedFiles()
            val localAssetNames = ArrayList<String>()

            localAssetList?.let {
                for (file in localAssetList) {
                    localAssetNames.add(file.name)
                }
            }

            val pendingAssets = assetList.filter { fileName ->
                !localAssetNames.contains(RapidStorageUtility.formatFileName(fileName))
            }

            return pendingAssets
        }

        /**
         * Compare local and server asset manifest and return `true` if they match,
         * `false` otherwise
         * @param networkVersion: version of new asset-manifest that is fetched
         */
        @JvmStatic
        fun compareAssetVersion(networkVersion: Int): Boolean {
            return networkVersion == RapidAssetCacheObject.getAssetVersion()
        }

        /**
         * Interacts with storage and writes asset data to cache disk
         */
        private fun writeAssetToDisk(url: String, assetBody: ResponseBody) {
            RapidStorageUtility.writeToAppCache(url, assetBody)
            onAssetDownloadWrittenToDisk(url)
        }
    }
}
