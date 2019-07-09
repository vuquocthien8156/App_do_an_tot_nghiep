package com.example.qthien.t__t.mvp.view.view_login

import com.example.qthien.t__t.model.Customer

interface ILogin {
    fun failure(message : String)
    fun resultRegisterAccount(idUser: Int?)
    fun resultLoginAccount(customer: Customer?)
    fun resultLoginPhone(customer: Customer?)
    fun resultLoginFacebook(customer: Customer?)
}