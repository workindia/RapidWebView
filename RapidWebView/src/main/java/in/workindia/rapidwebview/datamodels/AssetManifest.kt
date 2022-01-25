package `in`.workindia.rapidwebview.datamodels

import com.google.gson.annotations.SerializedName

data class AssetManifest(
    val version: String,
    @SerializedName("asset_urls") val assetUrls: Array<String>
)