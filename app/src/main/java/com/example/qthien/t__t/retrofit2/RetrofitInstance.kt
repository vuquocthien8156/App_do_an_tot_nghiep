package com.example.qthien.t__t.retrofit2

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance{
    private var retrofit : Retrofit? = null
    private val baseUrl = "https://api.nytimes.com/svc/search/v2/"

    val getRetrofit : GetData
        get(){
            if(retrofit == null){

                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(GetData::class.java)
        }
}