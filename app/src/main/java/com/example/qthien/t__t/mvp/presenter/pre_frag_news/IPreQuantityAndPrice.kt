package com.example.qthien.t__t.mvp.presenter.pre_frag_news

interface IPreQuantityAndPrice {
    fun failureQuantityAndPrice(message : String)
    fun successQuantityAndPrice(quantity : Int , price : Int)
}