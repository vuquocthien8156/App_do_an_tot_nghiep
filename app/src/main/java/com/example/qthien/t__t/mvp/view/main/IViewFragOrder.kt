package com.example.qthien.t__t.mvp.view.main

import com.example.qthien.t__t.model.CatalogyProduct
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.model.Product

interface IViewFragOrder {
    fun successGetLocationAddress(address : String, latLng: com.google.android.gms.maps.model.LatLng)
    fun failure(message: String)
    fun resultGetAddressDefault(infoAddress: InfoAddress?)
    fun resultGetAllCatalogy(arrCatalogy : ArrayList<CatalogyProduct>?)
    fun resultGetProductsBestBuy(arrProduct : ArrayList<Product>?)
    fun resultGetProductsByCatalogy(arrProduct : ArrayList<Product>?)
    fun resultGetProductBestFavorite(arrProduct : ArrayList<Product>?)
}