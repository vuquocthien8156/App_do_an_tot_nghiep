package com.example.qthien.t__t.interactor

import android.util.Log
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.model.ResponseLogin
import com.example.qthien.t__t.presenter.pre_login.IPreLogin
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InLogin(var iPreLogin: IPreLogin) {

    val instance = RetrofitInstance.getRetrofit

    fun login(email : String , pass : String){
        val call = instance.loginUser(email , pass)
        call.enqueue(object : Callback<ResponseLogin>{
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.d("responseeeeeFail" , t.toString())
            }

            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                Log.d("responseeeee" , response.body().toString())
                val r = response.body()
                if(r?.status.equals("ok"))
                    iPreLogin.resultLoginAccount(r?.customer)
                else
                    iPreLogin.resultLoginAccount(null)
            }
        })
    }

    fun loginPhone(phone : String){
        val call = instance.loginUserPhone(phone.replace("+" , ""))
        call.enqueue(object : Callback<ResponseLogin>{
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.d("responseeeeeFail" , t.toString())
            }

            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                Log.d("responseeeee" , response.body().toString())
                Log.d("responseeeee" , response.toString())
                Log.d("responseeeee" , phone)
                val r = response.body()
                if(r?.status.equals("ok")) {
                    iPreLogin.resultLoginPhone(r?.customer)
                }
                else
                    iPreLogin.resultLoginPhone(null)
            }
        })
    }

    fun loginFacebook(id_fb : String , email : String , name : String , url : String){
        val call = instance.loginUserFacebook(id_fb , email , name , url)
        call.enqueue(object : Callback<ResponseLogin>{
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.d("responseeeeeFail" , t.toString())
            }

            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                Log.d("responseeeee" , response.body().toString())
                Log.d("responseeeee" , response.toString())

                val r = response.body()
                if(r?.status.equals("ok")) {
                    iPreLogin.resultLoginFacebook(r?.customer)
                }
                else
                    iPreLogin.resultLoginFacebook(null)
            }
        })
    }

    fun register(customer : Customer){
        val call = instance.registerUser(customer.email.toString() ,
            customer.password.toString() ,
            customer.nameCustomer.toString() ,
            customer.gender!! ,
            customer.birthday.toString() ,
            customer.phoneNumber.toString())
        call.enqueue(object : Callback<com.example.qthien.t__t.model.ResponseLogin>{
            override fun onFailure(call: Call<com.example.qthien.t__t.model.ResponseLogin>, t: Throwable) {
                iPreLogin.failure(t.message.toString())
            }

            override fun onResponse(call: Call<com.example.qthien.t__t.model.ResponseLogin>, response: Response<ResponseLogin>) {
                val r = response.body()
                if(r?.status.equals("ok")){
                    iPreLogin.resultRegisterAccount(r?.customer?.idCustomer)
                }
                else
                    iPreLogin.resultRegisterAccount(null)
            }

        })
    }


    fun getInfoByEmail(email: String) {
        val call = instance.getInfoByEmail(email)
        call.enqueue(object : Callback<ResponseLogin>{
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                iPreLogin.failure(t.message.toString())
            }

            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                val r = response.body()
                if(r != null && r.status.equals("ok"))
                    iPreLogin.resultLoginAccount(r.customer)
                else
                    iPreLogin.resultLoginAccount(null)
            }
        })
    }
}