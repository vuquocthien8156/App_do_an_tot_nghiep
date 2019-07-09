package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseQuantityPrice
import com.example.qthien.t__t.mvp.presenter.pre_frag_news.IPreQuantityAndPrice
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InQuantityAndPrice(var iPreQuantityAndPrice: IPreQuantityAndPrice) {
    fun getQuantityAndPrice(idCustomer : Int){
        val call = RetrofitInstance.getRetrofit.getTotalQuantityAndTotalPrice(idCustomer)
        call.enqueue(object : Callback<ResponseQuantityPrice>{
            override fun onFailure(call: Call<ResponseQuantityPrice>, t: Throwable) {
                iPreQuantityAndPrice.failureQuantityAndPrice(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseQuantityPrice>, response: Response<ResponseQuantityPrice>) {
                val r = response.body()
                Log.d("totallllllllllllllll" , r?.totalPrice.toString() + r?.totalQuantity.toString())
                if(r != null)
                    iPreQuantityAndPrice.successQuantityAndPrice(r.totalQuantity , r.totalPrice)
                else
                    iPreQuantityAndPrice.successQuantityAndPrice(-1 , -1)
            }
        })
    }
}