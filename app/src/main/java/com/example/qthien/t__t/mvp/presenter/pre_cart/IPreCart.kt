package com.example.qthien.t__t.mvp.presenter.pre_cart

import com.example.qthien.t__t.model.MainProductCart

interface IPreCart {
    fun getCartOfUser(arrCart: ArrayList<MainProductCart>?)
    fun failure(message : String)
}