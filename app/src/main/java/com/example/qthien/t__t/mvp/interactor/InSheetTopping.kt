package com.example.qthien.t__t.mvp.interactor

import com.example.qthien.t__t.model.ResponseProducts
import com.example.qthien.t__t.mvp.presenter.pre_cart.IPreSheetTopping
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InSheetTopping(var iPreSheetTopping: IPreSheetTopping) {
    var instance = RetrofitInstance.getRetrofit

    fun getAllProductTopping(){
        val call = instance.getAllProductByMainCate(3)
        call.enqueue(object : Callback<ResponseProducts> {
            override fun onFailure(call: Call<ResponseProducts>, t: Throwable) {
                iPreSheetTopping.failureTopping(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseProducts>, response: Response<ResponseProducts>) {
                val r = response.body()
                if(r != null){
                    if(r.status.equals("ok")){
                        iPreSheetTopping.getTopping(r.arrProduct)
                    }
                    else
                        iPreSheetTopping.getTopping(null)
                }
                else
                    iPreSheetTopping.getTopping(null)
            }
        })
    }

}