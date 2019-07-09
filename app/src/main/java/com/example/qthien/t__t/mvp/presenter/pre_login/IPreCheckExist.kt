package com.example.qthien.t__t.mvp.presenter.pre_login

interface IPreCheckExist {
    fun resultExistAccount(email : String? , id_fb : String? , phone : String?)
    fun failure(message : String)
}