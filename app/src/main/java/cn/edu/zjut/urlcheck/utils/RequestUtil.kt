package cn.edu.zjut.urlcheck.utils

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface RequestService {
    @Headers("Content-Type: application/json")
    @POST("checkURL")
    fun checkURL(@Body data: RequestBody):Call<ResponseBody>
}

class RequestUtil {
    companion object{
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        private var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://121.36.196.242:9998/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        var service: RequestService = retrofit.create(RequestService::class.java)
    }
}