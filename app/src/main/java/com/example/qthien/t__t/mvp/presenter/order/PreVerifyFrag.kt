package com.example.qthien.t__t.mvp.presenter.order

import com.example.qthien.t__t.model.OrderAdd
import com.example.qthien.t__t.mvp.interactor.InVerifyFrag
import com.example.qthien.t__t.mvp.view.order.IVerifiyFrag

class PreVerifyFrag(var iVerifyFrag: IVerifiyFrag) : IPreVerifyFrag {
    override fun failure(message: String) {
        iVerifyFrag.failure(message)
    }

    override fun successAddOrder(status: String?) {
        iVerifyFrag.successAddOrder(status)
    }

    fun addOrder(orderAdd : OrderAdd){
        InVerifyFrag(this).addOrder(orderAdd)
    }
}