package com.example.qthien.t__t.presenter.pre_cart

interface IPreAddCart {
    fun failureAddCart(message : String)
    fun successUpdateCart(message : String?)
    fun successDeleteCart(message : String?)
    fun successAddCart(status: String?)
}