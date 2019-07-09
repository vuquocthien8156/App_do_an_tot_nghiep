package com.example.qthien.t__t.mvp.presenter.pre_login

interface IPreForgotPass {
    fun sendComfirmEmail(status : String?)
    fun failure(message : String)
}