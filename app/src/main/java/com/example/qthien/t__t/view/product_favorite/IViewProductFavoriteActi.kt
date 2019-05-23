package com.example.qthien.t__t.view.product_favorite

import com.example.qthien.t__t.model.Product

interface IViewProductFavoriteActi {
    fun resultGetProductFavorite(arrResult: ArrayList<Product>?)
    fun favoriteProduct(resultCode : String)
    fun failureFavorite(message : String)
}