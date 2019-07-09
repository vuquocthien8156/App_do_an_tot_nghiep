package com.example.qthien.t__t.mvp.view.view_login

interface IForgotPass {
    fun sendComfirmEmail(status : String?)
    fun failure(message : String)
}