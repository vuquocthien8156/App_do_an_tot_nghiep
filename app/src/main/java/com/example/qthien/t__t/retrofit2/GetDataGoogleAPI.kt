package com.example.qthien.t__t.retrofit2

import com.example.qthien.t__t.model.ResponsePlace
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetDataGoogleAPI {
    @GET("?key={key}&inputtype=textquery&fields=photos,formatted_address,name")
    fun getPlaceByText(@Query("input") query : String ,
                       @Path("key") key : String) : Call<ArrayList<ResponsePlace>>
}