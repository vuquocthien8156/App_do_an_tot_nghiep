package com.example.qthien.t__t.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseLogin
import com.example.qthien.t__t.presenter.pre_login.IPreLogin
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InteractorLogin(var iPreLogin: IPreLogin) {
    fun loginAndGetUser(email : String , pass : String){
        val instance = RetrofitInstance.getRetrofit
        val call = instance.registerUser(email , pass)
        call.enqueue(object : Callback<ResponseLogin>{
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.d("responseeeeeFail" , t.toString())
            }

            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                Log.d("responseeeee" , response.body().toString())
                response.body()
            }
        })
    }

    fun checkExistAccount(email : String = "" , phone : String = ""){
        iPreLogin.resultExistAccount(true)
    }
}