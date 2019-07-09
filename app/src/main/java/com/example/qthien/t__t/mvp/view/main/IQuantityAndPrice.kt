package com.example.qthien.t__t.mvp.view.main

interface IQuantityAndPrice {
    fun failureQuantityAndPrice(message : String)
    fun successQuantityAndPrice(quantity : Int , price : Int)
}