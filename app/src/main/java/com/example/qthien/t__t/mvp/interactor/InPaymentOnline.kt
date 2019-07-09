package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.mvp.presenter.order.IPrePaymentOnline
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InPaymentOnline(var iPrePaymentOnline: IPrePaymentOnline) {
    fun createCharge(idToken : String , total : Long) {
        Log.d("createCharge" , idToken)
        val call = RetrofitInstance.getRetrofit.paymentOnline(idToken , total)
        call.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                Log.d("createCharge" , t.message.toString())
                iPrePaymentOnline.failure( t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val r = response.body()
                Log.d("createCharge" , response.toString())
                Log.d("createCharge" , r.toString())
                Log.d("createCharge" , r?.status.toString())

                if(r != null)
                    iPrePaymentOnline.successPaymentOnline(r.status)
                else
                    iPrePaymentOnline.successPaymentOnline(r?.status)
            }
        })
    }
}