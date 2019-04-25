package com.example.qthien.t__t.presenter.pre_frag_order

import com.example.qthien.t__t.model.LatLng

interface IPreFragOrder {
    fun successGetLocationAddress(address : String,latLng: LatLng)
    fun failure(message: String)
}