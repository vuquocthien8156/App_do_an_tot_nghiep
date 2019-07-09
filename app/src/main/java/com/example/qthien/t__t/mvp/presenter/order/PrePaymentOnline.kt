package com.example.qthien.t__t.mvp.presenter.order

import com.example.qthien.t__t.mvp.interactor.InPaymentOnline
import com.example.qthien.t__t.mvp.view.order.IPaymentOnline

class PrePaymentOnline(var iPaymentOnline: IPaymentOnline) : IPrePaymentOnline {
    override fun failure(message: String) {
        iPaymentOnline.failure(message)
    }

    override fun successPaymentOnline(status: String?) {
        iPaymentOnline.successPaymentOnline(status)
    }

    fun createCharge(idToken : String , total : Long) {
        InPaymentOnline(this).createCharge(idToken , total)
    }
}