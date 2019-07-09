package com.example.qthien.t__t.mvp.interactor

import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.mvp.presenter.pre_login.IPreForgotPass
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InForgotPass(var iPreForgotPass: IPreForgotPass) {
    fun sendEmailForgot(email : String){
        val call = RetrofitInstance.getRetrofit.sendEmailConfirm(email)
        call.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreForgotPass.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val r = response.body()
                if(r != null)
                    iPreForgotPass.sendComfirmEmail(r.status)
                else
                    iPreForgotPass.sendComfirmEmail(null)
            }
        })
    }
}