package com.example.qthien.t__t.mvp.view.order

interface IPaymentOnline {
    fun failure(message : String)
    fun successPaymentOnline(status : String?)
}