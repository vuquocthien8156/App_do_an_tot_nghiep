package com.example.qthien.t__t.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseInfoAddress
import com.example.qthien.t__t.presenter.pre_address_delivery.IPreDeliveryAddressActi
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InDeliveryAddressActi(var iPreDeliveryAddressActi: IPreDeliveryAddressActi) {
    val instance = RetrofitInstance.getRetrofit
    fun getAllAddressInfoUser(idAccount: Int) {
        val call = instance.getAllAddressByUser(idAccount)
        call.enqueue(object : Callback<ResponseInfoAddress> {
            override fun onFailure(call: Call<ResponseInfoAddress>, t: Throwable) {
                iPreDeliveryAddressActi.failureDeliveryAddress(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseInfoAddress>, response: Response<ResponseInfoAddress>) {
                val r = response.body()
                Log.d("arrAddressInfo" , response.toString())
                Log.d("arrAddressInfo" , r.toString())
                if(r != null){
                    iPreDeliveryAddressActi.resultGetAllAddressInfoUser(r.arrAddressInfo)
                }
            }
        })
    }
}