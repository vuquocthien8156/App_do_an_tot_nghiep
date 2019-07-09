package com.example.qthien.t__t.mvp.presenter.pre_customer

import com.example.qthien.t__t.mvp.interactor.InChangePass
import com.example.qthien.t__t.mvp.view.customer.IChangePass

class PreChangePass(var iChangePass : IChangePass) : IPreChangePass {
    override fun failure(message: String) {
        iChangePass.failure(message)
    }

    override fun successChangePass(status: String?) {
        iChangePass.successChangePass(status)
    }

    fun changePassword(idUser : Int , password : String){
        InChangePass(this).chanegPassword(idUser , password)
    }
}