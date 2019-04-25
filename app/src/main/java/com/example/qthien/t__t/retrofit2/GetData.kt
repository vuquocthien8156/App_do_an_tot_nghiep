package com.example.qthien.t__t.retrofit2

import com.example.qthien.t__t.model.ResponseLogin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GetData {

    @FormUrlEncoded
    @POST("/api")
    fun registerUser(@Field("user") email : String, @Field("pass") mat_khau : String) : Call<ResponseLogin>
}