package com.example.qthien.t__t.view.main

import com.example.qthien.t__t.model.CatalogyProduct
import com.example.qthien.t__t.model.LatLng
import com.example.qthien.t__t.model.Product

interface IViewFragOrder {
    fun successGetLocationAddress(address : String, latLng: LatLng)
    fun failure(message: String)
    fun resultGetAllCatalogy(arrCatalogy : ArrayList<CatalogyProduct>?)
    fun resultGetProductsBestBuy(arrProduct : ArrayList<Product>?)
    fun resultGetProductsByCatalogy(arrProduct : ArrayList<Product>?)
    fun resultGetProductBestFavorite(arrProduct : ArrayList<Product>?)
}