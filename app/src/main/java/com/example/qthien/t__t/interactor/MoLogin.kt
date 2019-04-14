package com.example.qthien.t__t.interactor

import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.presenter.login.IPreLogin

class MoLogin(var iPreLogin: IPreLogin) {
    fun loginAndGetUser(customer: Customer){

    }

    fun checkExistAccount(email : String = "" , phone : String = ""){
        iPreLogin.resultExistAccount(true)
    }
}