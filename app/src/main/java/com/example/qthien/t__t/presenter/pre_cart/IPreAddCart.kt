package com.example.qthien.t__t.presenter.pre_cart

interface IPreAddCart {
    fun failureAddCart(message : String)
    fun checkFavoriteProduct(check : Int?)
    fun successAddCart(status: String?)
}