package com.example.qthien.t__t.mvp.view.cart

import com.example.qthien.t__t.model.Product

interface IDialogSheetTopping {
    fun getTopping(arrTopping : ArrayList<Product>?)
    fun failureTopping(message : String)
}