package cn.edu.zjut.urlcheck.utils

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestService {
    @GET("getQRCode")
    fun getQRCode(@Query("mobile") sno:String): Call<ResponseBody>
}

class RequestUtil {
    companion object{
        private var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://mryb.zjut.edu.cn/htk/baseInfo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var service: RequestService = retrofit.create(RequestService::class.java)

    }
}