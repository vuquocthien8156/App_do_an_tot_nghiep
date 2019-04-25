package com.example.qthien.t__t.view.delivery_address

import com.example.qthien.t__t.model.Place

interface IViewSearchDelivery {
    fun successSearchPlace(arrPlace : ArrayList<Place>)
    fun failureSearchPlace(message : String)
}