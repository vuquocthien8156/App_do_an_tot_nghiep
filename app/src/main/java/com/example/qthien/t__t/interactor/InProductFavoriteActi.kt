package com.example.qthien.t__t.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.model.ResponseProducts
import com.example.qthien.t__t.presenter.pre_product_favorite.IPreProductFavoriteActi
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InProductFavoriteActi(var iPreProductFavoriteActi: IPreProductFavoriteActi) {
    val instance = RetrofitInstance.getRetrofit

    fun getProductFavorite(idUser : Int){
        val call = instance.getProductFavoritedByUser(idUser)
        call.enqueue(object : Callback<ResponseProducts>{
            override fun onFailure(call: Call<ResponseProducts>, t: Throwable) {
                iPreProductFavoriteActi.failure(t.message.toString())
            }

            override fun onResponse(
                call: Call<ResponseProducts>,
                response: Response<ResponseProducts>
            ) {
                val re = response.body()
                if(re?.status != null && re.status.equals("ok")){
                    iPreProductFavoriteActi.resultGetProductFavorite(re.arrProduct)
                }
            }
        })
    }

    fun favoriteProduct(idProduct : Int , idUser: Int , like : Int){
        val call = instance.favoriteProduct(idProduct , idUser , like)
        call.enqueue(object : Callback<ResponseDefault>{
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreProductFavoriteActi.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                Log.d("ressssssssss" , response.toString())
                val re = response.body()
                if(re != null){
                    iPreProductFavoriteActi.favoriteProduct(re.status ?: "fail")
                }
                else{
                    iPreProductFavoriteActi.favoriteProduct("fail")
                }
            }

        })
    }
}