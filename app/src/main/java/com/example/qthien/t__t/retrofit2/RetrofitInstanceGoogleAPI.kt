package com.example.qthien.t__t.retrofit2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceGoogleAPI {
    private var retrofit : Retrofit? = null
    private val baseUrl = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json"

    val getRetrofit : GetDataGoogleAPI
        get(){
            if(retrofit == null){

                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(GetDataGoogleAPI::class.java)
        }
}