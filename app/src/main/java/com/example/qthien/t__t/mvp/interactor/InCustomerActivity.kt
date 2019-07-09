package com.example.qthien.t__t.mvp.interactor

import android.util.Log
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.model.ResponseDefault
import com.example.qthien.t__t.model.ResponseUploadImage
import com.example.qthien.t__t.mvp.presenter.pre_customer.IPreCustomerActivity
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class InCustomerActivity(var iPreCustomerActivity: IPreCustomerActivity) {

    val instanse = RetrofitInstance.getRetrofit
    fun uploadInfoCustomer(customer: Customer) {
        val callUpdate = instanse.updateUser(
            customer.idCustomer,
            customer.email,
            customer.nameCustomer,
            customer.gender,
            customer.birthday,
            customer.phoneNumber,
            customer.avatar
        )
        callUpdate.enqueue(object : Callback<ResponseDefault> {
            override fun onFailure(call: Call<ResponseDefault>, t: Throwable) {
                iPreCustomerActivity.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseDefault>, response: Response<ResponseDefault>) {
                val re = response.body()
                Log.d("uploadImage", response.toString())
                Log.d("uploadImage", re.toString())
                if(re != null)
                    iPreCustomerActivity.resultUpdateUser(re.status)
            }
        })
    }

    fun uploadImage(fileImage: File?) {
        var body: MultipartBody.Part? = null
        if (fileImage != null) {
            val requestBody = RequestBody.create(MediaType.parse("mutilpart/fromdata"), fileImage)
            body = MultipartBody.Part.createFormData("avatar", fileImage.absolutePath, requestBody)
        }

        val uploadImage = instanse.uploadImage(body)
        uploadImage.enqueue(object : Callback<ResponseUploadImage> {

            override fun onFailure(call: Call<ResponseUploadImage>, t: Throwable) {
                Log.d("uploadImage", t.message.toString())
            }

            override fun onResponse(call: Call<ResponseUploadImage>, response: Response<ResponseUploadImage>) {
                val r = response.body()
                Log.d("uploadImage", r?.filename)
                if(r != null)
                    iPreCustomerActivity.resultUploadImage(r.filename)
            }
        })
    }
}