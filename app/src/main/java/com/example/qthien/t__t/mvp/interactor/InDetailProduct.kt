package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.model.ResponseEvaluation
import com.example.qthien.t__t.mvp.presenter.detail_product.IPreDetailProduct
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InDetailProduct(var iPreDetailProduct: IPreDetailProduct) {

    val instance = RetrofitInstance.getRetrofit
    fun callDetailOrEvaluation(idProduct : Int, idUser : Int , page : Int? , point : Int? , time : Int? , refresh: Int?){
        val call = instance.getDetailOrEvaluationForProduct(idProduct , idUser , page , point , time , refresh)
        call.enqueue(object : Callback<ResponseEvaluation>{
            override fun onFailure(call: Call<ResponseEvaluation>, t: Throwable) {
                iPreDetailProduct.failureCallDetail(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseEvaluation>, response: Response<ResponseEvaluation>) {
                val r = response.body()
                Log.d("detaillllll" , response.toString())
                Log.d("detaillllll" , r.toString())
                if(r != null)
                    iPreDetailProduct.successCallDetail(r)
            }
        })
    }

    fun addTks(idEvaluate : Int , idCustomer : Int){
        val call = instance.addThanks(idEvaluate , idCustomer)
        call.enqueue(object : Callback<ResponseDefault>{
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreDetailProduct.failureCallDetail(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val r = response.body()
                if(r != null)
                    iPreDetailProduct.successAddTks(r.status)
                else
                    iPreDetailProduct.successAddTks(null)
            }
        })
    }
}