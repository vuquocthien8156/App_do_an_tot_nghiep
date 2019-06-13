package com.example.qthien.t__t.interactor

import android.util.Log
import com.example.qthien.t__t.model.Evaluation
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.model.ResponseMutilImage
import com.example.qthien.t__t.presenter.detail_product.IPreEvaluation
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class InEvaluation(var iPreEvaluation: IPreEvaluation) {
    val instance = RetrofitInstance.getRetrofit

    fun addEvaluate(evaluate : Evaluation){
        val call = instance.addEvaluation(evaluate)
        call.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreEvaluation.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val r = response.body()
                Log.d("adddddEv" , response.toString())
                Log.d("adddddEv" , r?.status.toString())
                if(r != null && r.status != null)
                    iPreEvaluation.successAddEvaluation(r.status)
                else
                    iPreEvaluation.successAddEvaluation(null)
            }
        })
    }

    val addImagesEvaluate = fun(arrFile : ArrayList<File>){
        val arrMultipartBody : ArrayList<MultipartBody.Part> = ArrayList()
        for(file in arrFile){
            val requestBody = RequestBody.create(MediaType.parse("mutilpart/fromdata"), file)
            val body = MultipartBody.Part.createFormData("avatar", file.absolutePath, requestBody)
            arrMultipartBody.add(body)
        }

        val call = instance.addImagesEvalute(arrMultipartBody)
        call.enqueue(object : Callback<ResponseMutilImage> {
            override fun onFailure(call: Call<ResponseMutilImage>, t: Throwable) {
                iPreEvaluation.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseMutilImage>, response: Response<ResponseMutilImage>) {
                val r = response.body()
                if(r != null && r.equals("Success")){
                    iPreEvaluation.successAddImg(r.imagesUrl)
                }
                else
                    iPreEvaluation.successAddImg(ArrayList())
            }
        })
    }
}