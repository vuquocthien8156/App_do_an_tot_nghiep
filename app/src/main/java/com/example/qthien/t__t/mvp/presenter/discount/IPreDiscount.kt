package com.example.qthien.t__t.mvp.presenter.discount

import com.example.qthien.t__t.model.Discount

interface IPreDiscount {
    fun failure(message : String)
    fun successGetDiscount(arrDiscount : ArrayList<Discount>)
}