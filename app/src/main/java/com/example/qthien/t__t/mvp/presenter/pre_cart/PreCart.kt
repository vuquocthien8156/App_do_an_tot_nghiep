package com.example.qthien.t__t.mvp.presenter.pre_cart

import com.example.qthien.t__t.mvp.interactor.InCart
import com.example.qthien.t__t.model.MainProductCart
import com.example.qthien.t__t.mvp.view.cart.IViewCart

class PreCart(var iViewCart: IViewCart) : IPreCart {
    override fun getCartOfUser(arrCart: ArrayList<MainProductCart>?) {
        iViewCart.getCartOfUser(arrCart)
    }

    override fun failure(message: String) {
        iViewCart.failure(message)
    }

    fun getAllCartUser(idCustomer: Int) {
        InCart(this).getAllCartUser(idCustomer)
    }
}