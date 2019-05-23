package com.example.qthien.t__t.presenter.pre_frag_order

import android.content.Context
import com.example.qthien.t__t.interactor.InFragOrder
import com.example.qthien.t__t.model.CatalogyProduct
import com.example.qthien.t__t.model.LatLng
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.view.main.IViewFragOrder

class PreFragOrder(var context : Context , var iViewFragOrder: IViewFragOrder) : IPreFragOrder {
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

    fun getLocation(){
        interactorFragOrder.getLocation()
    }

    override fun successGetLocationAddress(address: String, latLng: LatLng) {
        iViewFragOrder.successGetLocationAddress(address , latLng)
    }

    override fun failure(message: String) {
        iViewFragOrder.failure(message)
    }
}