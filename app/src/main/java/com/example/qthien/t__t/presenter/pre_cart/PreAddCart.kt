package com.example.qthien.t__t.presenter.pre_cart

import com.example.qthien.t__t.interactor.InAddCart
import com.example.qthien.t__t.model.CartPlus
import com.example.qthien.t__t.model.MainProductCart
import com.example.qthien.t__t.view.cart.IAddToCartActi

class PreAddCart(var iAddToCartActi: IAddToCartActi) : IPreAddCart {
    override fun successUpdateCart(message: String?) {
        iAddToCartActi.successUpdateCart(message)
    }

    override fun successDeleteCart(message: String?) {
        iAddToCartActi.successDeleteCart(message)
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

    fun updateCart(cart : MainProductCart) {
        InAddCart(this).updateCart(cart)
    }

    fun deleteCart(idCart : Int) {
        InAddCart(this).deleteCart(idCart)
    }
}