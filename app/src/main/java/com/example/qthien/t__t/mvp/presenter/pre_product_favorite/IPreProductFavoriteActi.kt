package com.example.qthien.t__t.mvp.presenter.pre_product_favorite

import com.example.qthien.t__t.model.Product

interface IPreProductFavoriteActi {
    fun resultGetProductFavorite(arrResult: ArrayList<Product>?)
    fun favoriteProduct(resultCode : String)
    fun failure(message : String)
}