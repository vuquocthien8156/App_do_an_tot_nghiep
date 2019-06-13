package com.example.qthien.t__t.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseOrder
import com.example.qthien.t__t.presenter.order.IPreOrderHistory
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InOrder(var iPreOrder: IPreOrderHistory) {

    val instance = RetrofitInstance.getRetrofit

    fun getOrderByUser(id : Int){
        val call = instance.getOrderByUser(id)
        call.enqueue(object : Callback<ResponseOrder> {
            override fun onFailure(call: Call<ResponseOrder>, t: Throwable) {
                iPreOrder.failureOrderHistory(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseOrder>, response: Response<ResponseOrder>) {
                val r = response.body()
                Log.d("Orderrrr" , r.toString())
                if(r != null && r.status.equals("ok")){
                    iPreOrder.resultgGetOrderByAccount(r.arrOrder)
                }
                else
                    iPreOrder.resultgGetOrderByAccount(null)
            }
        })
    }
}