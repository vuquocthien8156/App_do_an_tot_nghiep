package com.example.qthien.t__t.mvp.view.discount

import com.example.qthien.t__t.model.Discount

interface IDiscount {
    fun failure(message : String)
    fun successGetDiscount(arrDiscount : ArrayList<Discount>)
}