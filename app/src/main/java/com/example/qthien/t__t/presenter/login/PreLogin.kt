package com.example.qthien.t__t.presenter.login

import com.example.qthien.t__t.interactor.MoLogin
import com.example.qthien.t__t.view.login.ILogin

class PreLogin(var iLogin : ILogin) : IPreLogin {
    val moLogin : MoLogin

    init {
        moLogin = MoLogin(this)
    }

    fun checkExistAccount(email : String = "" , phone : String = ""){
        moLogin.checkExistAccount(email , phone)
    }

    override fun resultExistAccount(boolean: Boolean) {
        iLogin.resultExistAccount(boolean)
    }
}