package com.example.qthien.t__t.view.view_login

interface ICheckExistAccount {
    fun resultExistAccount(email : String? , id_fb : String? , phone : String?)
    fun failure(message : String)
}