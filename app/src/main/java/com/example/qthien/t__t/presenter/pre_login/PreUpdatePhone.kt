package com.example.qthien.t__t.presenter.pre_login

import com.example.qthien.t__t.interactor.InUpdatePhone
import com.example.qthien.t__t.view.main.IUpdatePhone

class PreUpdatePhone(var iUpdatePhone: IUpdatePhone) : IPreUpdatePhone {
    override fun successUpdatePhone(status: String?) {
        iUpdatePhone.successUpdatePhone(status)
    }

    override fun failure(message: String) {
        iUpdatePhone.successUpdatePhone(message)
    }

    fun updatePhone(idUser : Int ,phone : String){
        InUpdatePhone(this).updatePhone(idUser , phone)
    }
}