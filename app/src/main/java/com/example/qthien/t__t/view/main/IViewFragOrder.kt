package com.example.qthien.t__t.view.main

import com.example.qthien.t__t.model.LatLng

interface IViewFragOrder {
    fun successGetLocationAddress(address : String, latLng: LatLng)
    fun failure(message: String)
}