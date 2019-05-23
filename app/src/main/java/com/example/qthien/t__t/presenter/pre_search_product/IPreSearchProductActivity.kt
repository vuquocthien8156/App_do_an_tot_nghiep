package com.example.qthien.t__t.presenter.pre_search_product

import com.example.qthien.t__t.model.Product

interface IPreSearchProductActivity {
    fun failure(message : String)
    fun resultGetAllProduct(arrResult : ArrayList<Product>?)
}