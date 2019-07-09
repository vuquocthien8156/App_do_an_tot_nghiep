package com.example.qthien.t__t.mvp.presenter.pre_cart

import com.example.qthien.t__t.mvp.interactor.InSheetTopping
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.mvp.view.cart.IDialogSheetTopping

class PreSheetTopping(var iDialogSheetTopping: IDialogSheetTopping) : IPreSheetTopping {
    override fun getTopping(arrTopping: ArrayList<Product>?) {
        iDialogSheetTopping.getTopping(arrTopping)
    }

    override fun failureTopping(message: String) {
        iDialogSheetTopping.failureTopping(message)
    }


    fun getAllProductTopping() {
        InSheetTopping(this).getAllProductTopping()
    }
}