package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseDetailOrder
import com.example.qthien.t__t.mvp.presenter.order.IPreDetailOrderHistory
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InOrderDetail(var iPreDetailOrder: IPreDetailOrderHistory) {
    val instance = RetrofitInstance.getRetrofit
    fun getOrderDetail(idOrder : Int) {
        val call = instance.getOrderDetail(idOrder)
        call.enqueue(object : Callback<ResponseDetailOrder> {
            override fun onFailure(call: Call<ResponseDetailOrder>, t: Throwable) {
                iPreDetailOrder.failureOrderDetail(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDetailOrder>, response: Response<ResponseDetailOrder>) {
                val r = response.body()
                if(r != null && r.status.equals("Success")){
                    Log.d("DetaillOrder" , r.toString())
                    iPreDetailOrder.resultGetOrderDetail(r.arrDetail)
                }
                else
                    iPreDetailOrder.resultGetOrderDetail(null)
            }
        })
    }
}