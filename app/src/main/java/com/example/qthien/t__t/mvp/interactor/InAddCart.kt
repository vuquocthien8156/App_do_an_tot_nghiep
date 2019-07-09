package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.CartPlus
import com.example.qthien.t__t.model.MainProductCart
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.mvp.presenter.pre_cart.IPreAddCart
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InAddCart(var iPreAddCart: IPreAddCart){
    var instance = RetrofitInstance.getRetrofit
    fun addCart(cart : CartPlus){
        val call = instance.addCart(cart)
        call.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreAddCart.failureAddCart(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val r = response.body()

                if(r != null)
                    iPreAddCart.successAddCart(r.status)
                else
                    iPreAddCart.successAddCart(null)
            }
        })
    }

    fun updateCart(cart : MainProductCart){
        val call = instance.updateCart(cart)
        call.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreAddCart.failureAddCart(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val r = response.body()
                Log.d("UpdateCarrtt" , response.toString())
                if(r != null)
                    iPreAddCart.successUpdateCart(r.status)
                else
                    iPreAddCart.successUpdateCart(null)
            }
        })
    }

    fun deleteCart(idCart : Int){
        val call = instance.deleteCart(idCart)
        call.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreAddCart.failureAddCart(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val r = response.body()
                if(r != null)
                    iPreAddCart.successDeleteCart(r.status)
                else
                    iPreAddCart.successDeleteCart(null)
            }
        })
    }
}