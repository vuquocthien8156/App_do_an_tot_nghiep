package com.example.qthien.t__t.retrofit2

import retrofit2.Call
import com.example.qthien.t__t.model.Customer
import retrofit2.http.GET

interface GetData {
    @GET("")
    fun registerUser() : Call<Customer>
}