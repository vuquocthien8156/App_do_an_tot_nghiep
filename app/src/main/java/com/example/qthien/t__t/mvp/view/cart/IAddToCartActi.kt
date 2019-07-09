package com.example.qthien.t__t.mvp.view.cart

interface IAddToCartActi {
    fun failureAddCart(message : String)
    fun successUpdateCart(message : String?)
    fun successDeleteCart(message : String?)
    fun successAddCart(status : String?)
}