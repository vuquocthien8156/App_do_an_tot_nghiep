package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponsePoint
import com.example.qthien.t__t.mvp.presenter.point.IPrePoint
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InPoint(var iPrePoint: IPrePoint) {
    val instance = RetrofitInstance.getRetrofit
    fun getAllPointByUser(idUser : Int) {
        val call = instance.getAllPointOfUser(idUser)
        call.enqueue(object : Callback<ResponsePoint>{
            override fun onFailure(call: Call<ResponsePoint>, t: Throwable) {
                iPrePoint.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponsePoint>, response: Response<ResponsePoint>) {
                val r = response.body()
                Log.d("getAllPointByUser" , r.toString())
                Log.d("getAllPointByUser" , response.toString())
                if(response.code() == 200)
                    if(r != null && r.status.equals("Success"))
                        iPrePoint.successgetAllPointByUser(r.arrPoint)
                else
                    iPrePoint.failure(response.message().toString())
            }

        })
    }
}