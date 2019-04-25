package com.example.qthien.t__t.presenter.pre_search_delivery

import com.example.qthien.t__t.model.Place

interface IPreSearchDelivery {
    fun successSearchPlace(arrPlace : ArrayList<Place>)
    fun failureSearchPlace(message : String)
}