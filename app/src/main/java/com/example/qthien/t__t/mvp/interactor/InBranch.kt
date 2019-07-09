package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.ResponseBranch
import com.example.qthien.t__t.mvp.presenter.branch.IPreBranch
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InBranch(var iPreBranch: IPreBranch) {
    val instance = RetrofitInstance.getRetrofit
    fun getBranchFolowArea(){
        val call = instance.getBranchFolowArea()
        call.enqueue(object : Callback<ResponseBranch> {
            override fun onFailure(call: Call<ResponseBranch>, t: Throwable) {
                iPreBranch.failureBranch(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseBranch>, response: Response<ResponseBranch>) {
                val r = response.body()
                Log.d("branchhhh" , r.toString())
                if(r != null && r.status.equals("Success"))
                    iPreBranch.getBranchFolow(r.arrBranch)
                else
                    iPreBranch.getBranchFolow(null)
            }
        })
    }
}