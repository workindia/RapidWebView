package `in`.workindia.rapidwebview.assetcache

object RapidAssetCacheObject {

    /**
     * assetMap stores list of asset-urls and status across each asset-url
     * { "asset1": 1(status), "asset2": 2 }
     */
    private var assetMap = HashMap<String, Int>()

    /**
     * assetVersion stores current version of assets stored in cache
     */
    private var assetVersion: Int = -1

    /**
     * Stores whether assets are currently being downloaded
     */
    private var assetCacheDownloadStatus: Int = RapidAssetCacheClient.STATUS_PENDING


    /**
     * setAssetVersion - Set asset-version
     * @param version current local cache version
     */
    fun setAssetVersion(version: Int) {
        assetVersion = version
        RapidStorageUtility.AssetVersion.set(assetVersion)
    }

    /**
     * getAssetVersion - Get current local cache version
     * @return version
     */
    fun getAssetVersion(): Int {
        return RapidStorageUtility.AssetVersion.get()
    }

    /**
     * setAssetUrl - Set asset-url and it's status
     * @param url asset-url in it's full format https://example.com/asset/index.js
     * @param status current status of the asset file (pending, success, failure)
     *
     * TODO: setAssetUrl needs to be synchronised to avoid issue where two set operations can
     * happen at same instance in time
     */
    fun setAssetUrl(url: String, status: Int) {
        assetMap[url] = status
    }

    /**
     * getAssetMap - Get asset-url hashmap
     * @return assetMap
     */
    fun getAssetMap(): HashMap<String, Int> {
        return assetMap
    }

    /**
     * getAssetMapSize - Get count of assets stored in assetMap
     * @return Int - assetMap.size
     */
    fun getAssetMapSize(): Int {
        return assetMap.size
    }

    /**
     * clearAssetMap - Remove all entries in assetMap
     */
    fun clearAssetMap() {
        assetMap.clear()
    }

    /**
     * Set current status of asset download process to either STATUS_PENDING or STATUS_SUCCESS
     */
    fun setAssetDownloadStatus(status: Int) {
        if (status != RapidAssetCacheClient.STATUS_PENDING && status != RapidAssetCacheClient.STATUS_SUCCESS) {
            throw IllegalArgumentException("status parameter should be either of STATUS_PENDING or STATUS_SUCCESS - found $status")
        }
        assetCacheDownloadStatus = status
    }

    /**
     * Returns current status of asset download process
     * @return `true` if process is successful and completed, returns `false` otherwise
     */
    fun getAssetDownloadStatus(): Boolean {
        return assetCacheDownloadStatus == RapidAssetCacheClient.STATUS_SUCCESS
    }
}