package com.example.qthien.t__t.mvp.presenter.order

interface IPreVerifyFrag {
    fun failure(message : String)
    fun successAddOrder(status : String?)
}