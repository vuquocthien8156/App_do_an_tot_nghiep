package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseCheckExist
import com.example.qthien.t__t.mvp.presenter.pre_login.IPreCheckExist
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InCheckExist(var iPreCheckExist: IPreCheckExist) {
    val instance = RetrofitInstance.getRetrofit

    fun checkExistAccount(word : String){
        val call = instance.checkExistUser(word)
        call.enqueue(object : Callback<ResponseCheckExist> {
            override fun onFailure(call: Call<ResponseCheckExist>, t: Throwable) {
                iPreCheckExist.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseCheckExist>, response: Response<ResponseCheckExist>) {
                val responseCheckExist = response.body()
                Log.d("responseCheckExist" , response.toString())
                Log.d("responseCheckExist" , response.body().toString())
                if(responseCheckExist?.status.equals("ok")){
                    iPreCheckExist.resultExistAccount(responseCheckExist?.email ?: "" ,
                            responseCheckExist?.id_fb ?: "" ,
                            responseCheckExist?.phone ?: "")
                }
                else
                    iPreCheckExist.resultExistAccount("" , "" , "")
            }

        })
    }

}