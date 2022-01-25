package `in`.workindia.rapidwebview.assetcache

import `in`.workindia.rapidwebview.RapidClient
import android.webkit.MimeTypeMap
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.*
import java.net.URLEncoder

class RapidStorageUtility {

    companion object {
        private const val CACHE_DIR = "rapidWebView"

        /**
         * Lists all files present in the rapid web view cache dir
         * @return list of files
         */
        @JvmStatic
        fun listCachedFiles(): Array<out File>? {
            val context = RapidClient.getInstance().getAppContext()
            val cachePath = File(context.cacheDir, CACHE_DIR)
            return cachePath.listFiles()
        }

        /**
         * Formats the fileUrl to a filename. It url encodes the file url so that it can be used as
         * a file name to be stored on cache disk.
         * @param fileUrl Full url of file eg. https://example.com/dist/1.js
         * @return url encoded string - https%3A%2F%2Fexample.com%2Fdist%2F1.js
         */
        @JvmStatic
        fun formatFileName(fileUrl: String): String {
            return URLEncoder.encode(fileUrl, "utf-8")
        }

        /**
         * Check if given asset is present in cache directory
         * @param url full url of asset file (eg https://example.com/dist/1.js)
         * @return `true` if file exists, `false` otherwise
         */
        @JvmStatic
        fun cacheExists(url: String): Boolean {
            val fileName = formatFileName(url)
            val fileList = listCachedFiles()
            fileList?.forEach { file ->
                if (file.name.equals(fileName))
                    return true
            }
            return false
        }

        /**
         * Return cached file instance
         * @param url full url of the asset
         * @return FileInputStream
         */
        @JvmStatic
        fun getCachedAsset(url: String): FileInputStream {
            val fileName = formatFileName(url)
            val file = getCacheFileInstance(fileName)
            return FileInputStream(file)
        }

        /**
         * Delete all assets stored in cache directory
         */
        @JvmStatic
        fun clearCachedAssets() {
            val fileList = listCachedFiles()
            fileList?.forEach {
                it.delete()
            }
        }

        /**
         * Return mimetype from url
         * @param url full url of the asset
         * @return mimetype derived from the full url, defaults to empty string if mimetype
         * cannot be derived
         */
        @JvmStatic
        fun getAssetMimeType(url: String): String {
            var type = ""
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: ""
            }
            return type
        }

        /**
         * Write to app cache
         * @param fileName Name of the file along with the url
         * @param fileContent content of the file to be written in the file to be stored
         */
        @JvmStatic
        fun writeToAppCache(
            fileName: String,
            fileContent: ResponseBody
        ): Boolean {
            val file = getCacheFileInstance(formatFileName(fileName))

            return writeResponseBodyToDisk(fileContent.byteStream(), file)
        }

        /**
         * Get instance of file for given file name
         * @param fileName name of file to be stored in cache
         * @return File object instance
         */
        private fun getCacheFileInstance(fileName: String): File {
            var cachePath = File(RapidClient.getInstance().getAppContext().cacheDir, CACHE_DIR)
            if (!cachePath.exists()) {
                cachePath.mkdir()
            }

            cachePath = File(cachePath, fileName)
            return cachePath
        }

        /**
         * write the content to file and save it on the disk
         * @param inputStream the content to be written on file
         * @param outputFile the path where the content is written
         * @return `true` if file written, `false` otherwise
         */
        private fun writeResponseBodyToDisk(
            inputStream: InputStream?,
            outputFile: File?
        ): Boolean {
            return try {
                var outputStream: OutputStream? = null
                try {
                    val fileReader = ByteArray(4096)
                    var fileSizeDownloaded: Long = 0
                    outputStream = FileOutputStream(outputFile)
                    while (true) {
                        val read = inputStream!!.read(fileReader)
                        if (read == -1) {
                            break
                        }
                        outputStream.write(fileReader, 0, read)
                        fileSizeDownloaded += read.toLong()

                    }
                    outputStream.flush()
                    true
                } catch (e: IOException) {
                    false
                } finally {
                    inputStream?.close()
                    outputStream?.close()
                }
            } catch (e: IOException) {
                false
            }
        }

        /**
         * returns the uri for the file
         * from the filename if file exist in cache
         */
        fun getImageUriFromFileName(shareImage: String): File {
            return getCacheFileInstance(shareImage)
        }
    }

    class AssetVersion {
        companion object {
            private const val CACHE_VERSION_FILE_NAME = "RapidWebView.version"

            /**
             * @return the int value of current asset version
             */
            @JvmStatic
            fun get(): String {
                val file = getCacheFileInstance(CACHE_VERSION_FILE_NAME)
                val versionValue = StringBuilder()
                try {
                    val br = BufferedReader(FileReader(file))
                    var line: String?
                    while (br.readLine().also { line = it } != null) {
                        versionValue.append(line)
                        versionValue.append('\n')
                    }
                    br.close()
                } catch (e: IOException) {
                    return "-1"
                }
                return versionValue.toString().trim()
            }

            /**
             * Store asset version in cache directory
             * @param version int value of asset version
             */
            @JvmStatic
            fun set(version: String) {
                writeToAppCache(
                    CACHE_VERSION_FILE_NAME,
                    version.toResponseBody(null)
                )
            }
        }
    }
}