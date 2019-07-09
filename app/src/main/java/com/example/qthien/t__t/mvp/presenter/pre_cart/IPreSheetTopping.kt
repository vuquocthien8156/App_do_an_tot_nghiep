package com.example.qthien.t__t.mvp.presenter.pre_cart

import com.example.qthien.t__t.model.Product

interface IPreSheetTopping {
    fun getTopping(arrTopping : ArrayList<Product>?)
    fun failureTopping(message : String)
}