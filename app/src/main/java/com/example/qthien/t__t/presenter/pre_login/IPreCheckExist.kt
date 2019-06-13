package com.example.qthien.t__t.presenter.pre_login

interface IPreCheckExist {
    fun resultExistAccount(email : String? , id_fb : String? , phone : String?)
    fun failure(message : String)
}