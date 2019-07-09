package com.example.qthien.t__t.mvp.interactor

import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.mvp.presenter.pre_login.IPreUpdatePhone
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InUpdatePhone(var iPreUpdatePhone: IPreUpdatePhone) {
    fun updatePhone(idUser : Int ,phone : String){
        val call = RetrofitInstance.getRetrofit.updatePhone(idUser , phone)
        call.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreUpdatePhone.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val r = response.body()
                if(r != null)
                    iPreUpdatePhone.successUpdatePhone(r.status)
                else
                    iPreUpdatePhone.successUpdatePhone(null)
            }
        })
    }
}