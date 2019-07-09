package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.OrderAdd
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.mvp.presenter.order.IPreVerifyFrag
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InVerifyFrag(var iPreVerifyFrag: IPreVerifyFrag) {
    fun addOrder(orderAdd: OrderAdd) {
        val call = RetrofitInstance.getRetrofit.addOrder(orderAdd)
        call.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                Log.d("addOrder" , t.message.toString())
                iPreVerifyFrag.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                Log.d("addOrder" , response.toString())
                Log.d("addOrder" , response.body().toString())
                val r = response.body()
                if(r != null){
                    iPreVerifyFrag.successAddOrder(r.status)
                }else{
                    iPreVerifyFrag.successAddOrder(r?.status)
                }
            }
        })
    }
}