package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseProducts
import com.example.qthien.t__t.mvp.presenter.pre_search_product.IPreSearchProductActivity
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InSearchProductActivity(var iPreSearchProductActivity: IPreSearchProductActivity) {
    val instance = RetrofitInstance.getRetrofit
    fun getAllProduct(){
        val call = instance.getAllProduct()
        call.enqueue(object : Callback<ResponseProducts>{
            override fun onFailure(call: Call<ResponseProducts>, t: Throwable) {
                iPreSearchProductActivity.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseProducts>, response: Response<ResponseProducts>) {
                Log.d("resultttt" , response.body()?.arrProduct.toString())
                val products = response.body()?.arrProduct
                iPreSearchProductActivity.resultGetAllProduct(products)
            }
        })
    }
}