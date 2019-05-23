package com.example.qthien.t__t.presenter.pre_login

import com.example.qthien.t__t.model.Customer

interface IPreLogin {
    fun failure(message : String)
    fun resultExistAccount(email : String? , id_fb : String? , phone : String?)
    fun resultRegisterAccount(idUser : Int?)
    fun resultLoginAccount(customer: Customer?)
    fun resultLoginPhone(customer: Customer?)
    fun resultLoginFacebook(customer: Customer?)
}