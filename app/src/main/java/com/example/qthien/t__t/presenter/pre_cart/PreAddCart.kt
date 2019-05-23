package com.example.qthien.t__t.presenter.pre_cart

import com.example.qthien.t__t.interactor.InAddCart
import com.example.qthien.t__t.model.CartPlus
import com.example.qthien.t__t.view.cart.IAddToCartActi

class PreAddCart(var iAddToCartActi: IAddToCartActi) : IPreAddCart {

    override fun checkFavoriteProduct(check: Int?) {
        iAddToCartActi.checkFavoriteProduct(check)
    }

    override fun failureAddCart(message: String) {
        iAddToCartActi.failureAddCart(message)
    }

    override fun successAddCart(status: String?) {
        iAddToCartActi.successAddCart(status)
    }

    // fun
    fun addCart(cart: CartPlus) {
        InAddCart(this).addCart(cart)
    }

    fun checkFavoriteProduct(idUser : Int , idProduct : Int) {
        InAddCart(this).checkFavoriteProduct(idUser , idProduct)
    }
}