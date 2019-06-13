package com.example.qthien.t__t.presenter.pre_frag_news

import com.example.qthien.t__t.interactor.InQuantityAndPrice
import com.example.qthien.t__t.view.main.IQuantityAndPrice

class PreQuantityAndPrice(var iQuantityAndPrice: IQuantityAndPrice) : IPreQuantityAndPrice{
    override fun failureQuantityAndPrice(message: String) {
        iQuantityAndPrice.failureQuantityAndPrice(message)
    }

    override fun successQuantityAndPrice(quantity: Int, price: Int) {
        iQuantityAndPrice.successQuantityAndPrice(quantity , price)
    }

    fun getQuantityAndPrice(idCustomer : Int){
        InQuantityAndPrice(this).getQuantityAndPrice(idCustomer)
    }
}