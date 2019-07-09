package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseNews
import com.example.qthien.t__t.mvp.presenter.pre_frag_news.IPreFragNews
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InFragNews(var iPreFragNews: IPreFragNews) {
    fun getNews(page : Int){
        val call = RetrofitInstance.getRetrofit.getNews(if(page == 0) null else page)
        call.enqueue(object : Callback<ResponseNews>{
            override fun onFailure(call: Call<ResponseNews>, t: Throwable) {
                iPreFragNews.failureNews(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseNews>, response: Response<ResponseNews>) {
                val r = response.body()
                Log.d("newsssss" , r.toString())
                Log.d("newsssss" , response.toString())
                if(r != null && r.status.equals("Success")){
                    iPreFragNews.successGetNews(r.arrNew)
                }
                else
                    iPreFragNews.successGetNews(arrayListOf())
            }
        })
    }
}