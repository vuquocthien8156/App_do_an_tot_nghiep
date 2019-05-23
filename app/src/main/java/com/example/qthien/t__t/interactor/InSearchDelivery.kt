package com.example.qthien.t__t.interactor

import android.content.Context
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.ResponsePlace
import com.example.qthien.t__t.presenter.pre_search_delivery.PreSearchDelivery
import com.example.qthien.t__t.retrofit2.GetDataGoogleAPI
import com.example.qthien.t__t.retrofit2.RetrofitInstanceGoogleAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InSearchDelivery(
    internal var context: Context,
    internal var preSearchDelivery: PreSearchDelivery
) {
    val instance : GetDataGoogleAPI

    init {
        instance = RetrofitInstanceGoogleAPI.getRetrofit
    }

    fun searchPlace(address : String){
        val call = instance.getPlaceByText(address , context.getString(R.string.GOOGLE_API_KEY))
        call.enqueue(object : Callback<ArrayList<ResponsePlace>> {
            override fun onFailure(call: Call<ArrayList<ResponsePlace>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<ResponsePlace>>, response: Response<ArrayList<ResponsePlace>>) {

            }

        })
    }
}