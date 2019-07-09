package com.example.qthien.t__t.mvp.view.search_product

import com.example.qthien.t__t.model.Product

interface IViewSearchProductActivity {
    fun resultGetAllProduct(arrResult : ArrayList<Product>?)
    fun failure(message : String)
}