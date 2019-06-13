package com.example.qthien.t__t.view.main

interface IQuantityAndPrice {
    fun failureQuantityAndPrice(message : String)
    fun successQuantityAndPrice(quantity : Int , price : Int)
}