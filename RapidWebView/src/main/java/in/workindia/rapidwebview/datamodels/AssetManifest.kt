package `in`.workindia.rapidwebview.datamodels

import com.google.gson.annotations.SerializedName

data class AssetManifest(
    val version: Int,
    @SerializedName("asset_urls") val assetUrls: Array<String>
)