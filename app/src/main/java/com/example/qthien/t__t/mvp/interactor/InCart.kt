package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseCart
import com.example.qthien.t__t.mvp.presenter.pre_cart.IPreCart
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InCart(var iPreCart: IPreCart) {

    val instance = RetrofitInstance.getRetrofit
    fun getAllCartUser(idCustomer : Int){
        val call = instance.getCartOfUser(idCustomer)
        call.enqueue(object : Callback<ResponseCart>{
            override fun onFailure(call: Call<ResponseCart>, t: Throwable) {
                iPreCart.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseCart>, response: Response<ResponseCart>) {
                val r = response.body()
                Log.d("toppinnnngg" , response.body()?.arrCart.toString())
                if(r != null && r.status.equals("Success"))
                    iPreCart.getCartOfUser(r.arrCart)
                else
                    iPreCart.getCartOfUser(null)
            }
        })
    }
}