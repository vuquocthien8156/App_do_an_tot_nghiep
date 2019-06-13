package com.example.qthien.t__t.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.presenter.pre_customer.IPreChangePass
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InChangePass(var iPreChangePass: IPreChangePass) {
    val instance = RetrofitInstance.getRetrofit
    fun chanegPassword(idUser : Int , password : String){
        val call = instance.changePass(idUser , password)
        call.enqueue(object : Callback<ResponseDefault>{
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreChangePass.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val r = response.body()
                Log.d("changeeeepass" , r.toString())
                Log.d("changeeeepass" , response.toString())
                if(r != null)
                    iPreChangePass.successChangePass(r.status)
                else
                    iPreChangePass.successChangePass(null)
            }
        })
    }
}