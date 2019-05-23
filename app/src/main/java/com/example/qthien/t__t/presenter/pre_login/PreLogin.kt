package com.example.qthien.t__t.presenter.pre_login

import com.example.qthien.t__t.interactor.InLogin
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.view.view_login.ILogin

class PreLogin(var iLogin : ILogin) : IPreLogin {

    val moLogin : InLogin

    init {
        moLogin = InLogin(this)
    }

    fun login(email : String , pass : String){
        moLogin.login(email , pass)
    }

    fun getInfoByEmail(email : String ){
        moLogin.getInfoByEmail(email)
    }

    fun loginPhoneUser(phone : String){
        moLogin.loginPhone(phone)
    }

    fun checkExistAccount(word : String){
        moLogin.checkExistAccount(word)
    }

    fun register(customer : Customer) {
        moLogin.register(customer)
    }

    fun loginFacebook(id_fb : String , email : String , name : String){
        moLogin.loginFacebook(id_fb , email , name)
    }

    override fun resultExistAccount(email : String? , id_fb : String? , phone : String?) {
        iLogin.resultExistAccount(email , id_fb , phone)
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