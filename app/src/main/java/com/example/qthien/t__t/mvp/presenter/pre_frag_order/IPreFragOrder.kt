package com.example.qthien.t__t.mvp.presenter.pre_frag_order

import com.example.qthien.t__t.model.CatalogyProduct
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.model.Product
import com.google.android.gms.maps.model.LatLng

interface IPreFragOrder {
    fun successGetLocationAddress(address : String,latLng: LatLng)
    fun failure(message: String)
    fun resultGetAddressDefault(infoAddress: InfoAddress?)
    fun resultGetAllCatalogy(arrCatalogy : ArrayList<CatalogyProduct>?)
    fun resultGetProductsBestBuy(arrProduct : ArrayList<Product>?)
    fun resultGetProductsByCatalogy(arrProduct : ArrayList<Product>?)
    fun resultGetProductBestFavorite(arrProduct : ArrayList<Product>?)
}