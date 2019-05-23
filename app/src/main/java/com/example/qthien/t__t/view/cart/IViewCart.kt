package com.example.qthien.t__t.view.cart

import com.example.qthien.t__t.model.MainProductCart

interface IViewCart {
    fun getCartOfUser(arrCart: ArrayList<MainProductCart>?)
    fun failure(message : String)
}