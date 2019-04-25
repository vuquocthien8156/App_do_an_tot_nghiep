package com.example.qthien.t__t.retrofit2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance{
    private var retrofit : Retrofit? = null
    private val baseUrl = "http://192.168.1.4:8000"

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