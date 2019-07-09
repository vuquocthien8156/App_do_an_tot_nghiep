package com.example.qthien.t__t.mvp.presenter.pre_frag_order

import android.content.Context
import com.example.qthien.t__t.model.CatalogyProduct
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.mvp.interactor.InFragOrder
import com.example.qthien.t__t.mvp.view.main.IViewFragOrder

class PreFragOrder(var context : Context , var iViewFragOrder: IViewFragOrder) : IPreFragOrder {
    override fun resultGetAddressDefault(infoAddress: InfoAddress?) {
        iViewFragOrder.resultGetAddressDefault(infoAddress)
    }

    override fun resultGetProductBestFavorite(arrProduct: ArrayList<Product>?) {
        iViewFragOrder.resultGetProductBestFavorite(arrProduct)
    }

    override fun resultGetAllCatalogy(arrCatalogy: ArrayList<CatalogyProduct>?) {
        iViewFragOrder.resultGetAllCatalogy(arrCatalogy)
    }

    override fun resultGetProductsBestBuy(arrProduct: ArrayList<Product>?) {
        iViewFragOrder.resultGetProductsBestBuy(arrProduct)
    }

    override fun resultGetProductsByCatalogy(arrProduct: ArrayList<Product>?) {
        iViewFragOrder.resultGetProductsByCatalogy(arrProduct)
    }

    val interactorFragOrder : InFragOrder

    init {
        interactorFragOrder = InFragOrder(context , this)
    }

    // function
    fun getAddressInfo(idCus : Int){
        interactorFragOrder.getAddressInfo(idCus)
    }

    fun getProductsBestBuy() {
        interactorFragOrder.getProductsBestBuy()
    }

    fun getProductsByCatalogy(idCatelogy: Int?){
        interactorFragOrder.getProductsByCatalogy(idCatelogy)
    }

    fun getAllCatalogy(){
        interactorFragOrder.getAllCatalogy()
    }

    fun getProductBestFavorite(){
        interactorFragOrder.getProductBestFavorite()
    }

    fun removeRequest(){
        interactorFragOrder.removeRequest()
    }

    fun getLocation(){
        interactorFragOrder.getLocation()
    }

    override fun successGetLocationAddress(address: String, latLng: com.google.android.gms.maps.model.LatLng) {
        iViewFragOrder.successGetLocationAddress(address , latLng)
    }

    override fun failure(message: String) {
        iViewFragOrder.failure(message)
    }
}