package com.example.qthien.t__t.interactor

import android.util.Log
import com.example.qthien.t__t.model.ChildEvaluation
import com.example.qthien.t__t.model.ResponseChildEvaluation
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.presenter.detail_product.IPreDetailEvaluation
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InEvaluationChild(var iPreDetailEvaluation: IPreDetailEvaluation) {
    val instance = RetrofitInstance.getRetrofit
    fun getChildEvalute(idEvaluate : Int , page : Int){
        val call = instance.getChildEvaluate(idEvaluate , page)
        call.enqueue(object : Callback<ResponseChildEvaluation> {
            override fun onFailure(call: Call<ResponseChildEvaluation>, t: Throwable) {
                iPreDetailEvaluation.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseChildEvaluation>, response: Response<ResponseChildEvaluation>) {
                val r = response.body()
                Log.d("childddddddddddd", response.toString())
                if(r != null && r.status.equals("Success"))
                    iPreDetailEvaluation.successGetAllChildEvaluation(r.arrChildEvaluation)
            }
        })
    }

    fun addReplyForEvaluation(childevaluate : ChildEvaluation){
        val call = instance.addReply(childevaluate)
        call.enqueue(object : Callback<ResponseDefault>{
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreDetailEvaluation.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val r = response.body()
                Log.d("adddddChilđ" , response.toString())
                Log.d("adddddChilđ" , r.toString())
                if(r != null && r.status != null)
                    iPreDetailEvaluation.succressAddChild(r.status)
                else
                    iPreDetailEvaluation.succressAddChild(null)
            }
        })
    }
}