package com.example.qthien.t__t.retrofit2

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance{
    private var retrofit : Retrofit? = null
    val baseUrl = "http://192.168.0.102:8000"

    val getRetrofit : GetData
        get(){
            if(retrofit == null){

                val client = OkHttpClient.Builder()
                    .addNetworkInterceptor(StethoInterceptor())
                    .build();

                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(GetData::class.java)
        }
}