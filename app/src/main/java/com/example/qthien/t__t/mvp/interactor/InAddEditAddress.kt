package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.mvp.presenter.pre_address_delivery.IPreAddEditAddressDialog
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InAddEditAddress(var iPreAddEditAddress: IPreAddEditAddressDialog) {
    val instance = RetrofitInstance.getRetrofit

    fun addAddressInfo(infoAddress : InfoAddress){
        val call = instance.insertAddressByUser(infoAddress)
        call.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreAddEditAddress.failureEditCreateAddress(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                Log.d("AddUPAddressInfo" , response.toString())
                Log.d("AddUPAddressInfo" , response.body().toString())
                if(response.body() != null)
                    iPreAddEditAddress.resultEditOrCreateAddress(response.body()?.status)
            }
        })
    }

    fun updateAddressInfo(infoAddress : InfoAddress){
        val call = instance.updateAddressByUser(infoAddress)
        call.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreAddEditAddress.failureEditCreateAddress(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                Log.d("AddUPAddressInfo" , response.toString())
                Log.d("AddUPAddressInfo" , response.body()?.status.toString())
                if(response.body() != null)
                    iPreAddEditAddress.resultEditOrCreateAddress(response.body()?.status)
            }
        })
    }
}