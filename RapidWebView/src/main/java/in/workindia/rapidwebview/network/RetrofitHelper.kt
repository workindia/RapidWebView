package `in`.workindia.rapidwebview.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Singleton Retrofit class to create single instance of Retrofit
 */

object RetrofitHelper {
    private fun retrofitService(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    val createService: Urls by lazy {
        retrofitService().create(Urls::class.java)
    }

}

interface Urls {
    @GET()
    fun getAsset(@Url assetUrl: String): Call<ResponseBody>

    @POST()
    fun uploadFileViaPost(@Url uploadUrl: String, @Body requestBody: RequestBody): Call<Void>

    @PUT()
    fun uploadFileViaPut(@Url uploadUrl: String, @Body requestBody: RequestBody): Call<Void>
}