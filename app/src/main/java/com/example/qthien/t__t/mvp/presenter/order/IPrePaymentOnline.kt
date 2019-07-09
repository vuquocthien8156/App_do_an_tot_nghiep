package com.example.qthien.t__t.mvp.presenter.order

interface IPrePaymentOnline {
    fun failure(message : String)
    fun successPaymentOnline(status : String?)
}