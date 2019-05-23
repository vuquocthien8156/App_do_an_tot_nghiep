package com.example.qthien.t__t.view.cart

interface IAddToCartActi {
    fun failureAddCart(message : String)
    fun checkFavoriteProduct(check : Int?)
    fun successAddCart(status : String?)
}