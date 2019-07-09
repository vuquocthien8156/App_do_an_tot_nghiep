package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseDiscount
import com.example.qthien.t__t.mvp.presenter.discount.IPreDiscount
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InDiscount(var iPreDiscount: IPreDiscount) {
    val instance = RetrofitInstance.getRetrofit
    fun getDiscount(slider : Int?){
        val call = instance.getDiscount(slider)
        call.enqueue(object : Callback<ResponseDiscount> {
            override fun onFailure(call: Call<ResponseDiscount>, t: Throwable) {
                iPreDiscount.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDiscount>, response: Response<ResponseDiscount>) {
                val r = response.body()
                Log.d("getDiscount" , r.toString())
                Log.d("getDiscount" , response.toString())
                if(r != null)
                    iPreDiscount.successGetDiscount(r.arrDiscount)
                else
                    iPreDiscount.successGetDiscount(arrayListOf())
            }
        })
    }
}