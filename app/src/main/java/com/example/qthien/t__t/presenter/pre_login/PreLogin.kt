package com.example.qthien.t__t.presenter.pre_login

import com.example.qthien.t__t.interactor.InteractorLogin
import com.example.qthien.t__t.view.view_login.ILogin

class PreLogin(var iLogin : ILogin) : IPreLogin {
    val moLogin : InteractorLogin

    init {
        moLogin = InteractorLogin(this)
    }

    fun loginAndGetUser(email : String , pass : String){
        moLogin.loginAndGetUser(email , pass)
    }

        fun checkExistAccount(email : String = "" , phone : String = ""){
        moLogin.checkExistAccount(email , phone)
    }

    override fun resultExistAccount(boolean: Boolean) {
        iLogin.resultExistAccount(boolean)
    }
}