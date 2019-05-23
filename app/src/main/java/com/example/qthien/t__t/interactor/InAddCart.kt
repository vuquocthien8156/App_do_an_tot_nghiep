package com.example.qthien.t__t.interactor

import com.example.qthien.t__t.model.CartPlus
import com.example.qthien.t__t.model.ResponseCheckFavorite
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.presenter.pre_cart.IPreAddCart
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

    fun checkFavoriteProduct(idUser : Int , idProduct : Int){
        val call = instance.checkFavoriteProduct(idUser , idProduct)
        call.enqueue(object : Callback<ResponseCheckFavorite>{
            override fun onFailure(call: Call<ResponseCheckFavorite>, t: Throwable) {
                iPreAddCart.failureAddCart(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseCheckFavorite>, response: Response<ResponseCheckFavorite>) {
                val r = response.body()
                if(r != null){
                    if(r.status.equals("ok")){
                        iPreAddCart.checkFavoriteProduct(r.check)
                    }
                    else
                        iPreAddCart.checkFavoriteProduct(null)
                }
                else
                    iPreAddCart.checkFavoriteProduct(null)
            }
        })
    }
}