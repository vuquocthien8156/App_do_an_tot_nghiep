package com.example.qthien.t__t.presenter.pre_login

import android.util.Log
import com.example.qthien.t__t.interactor.InLogin
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.view.view_login.ILogin
import java.math.BigInteger
import java.security.MessageDigest

class PreLogin(var iLogin : ILogin) : IPreLogin {

    val moLogin : InLogin

    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    init {
        moLogin = InLogin(this)
    }

    fun login(email : String , pass : String){
        Log.d("Mdđ5" , pass.md5())
        moLogin.login(email , pass.md5())
    }

    fun getInfoByEmail(email : String){
        moLogin.getInfoByEmail(email)
    }

    fun loginPhoneUser(phone : String){
        moLogin.loginPhone(phone)
    }

    fun register(customer : Customer) {
        if(!(customer.password ?: "").equals(""))
            customer.password = customer.password?.md5()
        moLogin.register(customer)
    }

    fun loginFacebook(id_fb : String , email : String , name : String , url : String){
        moLogin.loginFacebook(id_fb , email , name , url)
    }


    override fun failure(message: String) {
        iLogin.failure(message)
    }

    override fun resultRegisterAccount(idUser: Int?) {
        iLogin.resultRegisterAccount(idUser)
    }

    override fun resultLoginAccount(customer: Customer?) {
        iLogin.resultLoginAccount(customer)
    }

    override fun resultLoginPhone(customer: Customer?) {
        iLogin.resultLoginPhone(customer)
    }

    override fun resultLoginFacebook(customer: Customer?){
        iLogin.resultLoginFacebook(customer)
    }

}