package com.example.qthien.t__t.mvp.presenter.pre_login

import com.example.qthien.t__t.mvp.interactor.InForgotPass
import com.example.qthien.t__t.mvp.view.view_login.IForgotPass

class PreForgotPass(var iForgotPass: IForgotPass) : IPreForgotPass {
    override fun sendComfirmEmail(status: String?) {
        iForgotPass.sendComfirmEmail(status)
    }

    override fun failure(message: String) {
        iForgotPass.failure(message)
    }

    fun sendEmailForgot(email : String){
        InForgotPass(this).sendEmailForgot(email)
    }
}