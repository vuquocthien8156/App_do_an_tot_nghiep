package com.example.qthien.t__t.mvp.presenter.pre_login

import com.example.qthien.t__t.model.Customer

interface IPreLogin {
    fun failure(message : String)
    fun resultRegisterAccount(idUser : Int?)
    fun resultLoginAccount(customer: Customer?)
    fun resultLoginPhone(customer: Customer?)
    fun resultLoginFacebook(customer: Customer?)
}