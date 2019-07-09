package com.example.qthien.t__t.mvp.view.delivery_address

import com.example.qthien.t__t.model.Place

interface IViewSearchDelivery {
    fun successSearchPlace(arrPlace : ArrayList<Place>)
    fun failureSearchPlace(message : String)
}