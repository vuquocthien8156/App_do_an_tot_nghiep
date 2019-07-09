package com.example.qthien.t__t.mvp.presenter.discount

import com.example.qthien.t__t.mvp.interactor.InDiscount
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.mvp.view.discount.IDiscount

class PreDiscount(var iDiscount: IDiscount) : IPreDiscount {
    override fun failure(message: String) {
        iDiscount.failure(message)
    }

    override fun successGetDiscount(arrDiscount: ArrayList<Discount>) {
        iDiscount.successGetDiscount(arrDiscount)
    }

    fun getDiscount(slider: Int?) {
        InDiscount(this).getDiscount(slider)
    }
}