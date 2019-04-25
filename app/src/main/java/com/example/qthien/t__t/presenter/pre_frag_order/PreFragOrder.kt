package com.example.qthien.t__t.presenter.pre_frag_order

import android.content.Context
import com.example.qthien.t__t.interactor.InteractorFragOrder
import com.example.qthien.t__t.model.LatLng
import com.example.qthien.t__t.view.main.IViewFragOrder

class PreFragOrder(var context : Context , var iViewFragOrder: IViewFragOrder) : IPreFragOrder {

    val interactorFragOrder : InteractorFragOrder

    init {
        interactorFragOrder = InteractorFragOrder(context , this)
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