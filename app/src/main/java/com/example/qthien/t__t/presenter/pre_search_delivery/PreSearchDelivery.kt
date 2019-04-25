package com.example.qthien.t__t.presenter.pre_search_delivery

import android.content.Context
import com.example.qthien.t__t.interactor.InteractorSearchDelivery
import com.example.qthien.t__t.model.Place
import com.example.qthien.t__t.view.delivery_address.IViewSearchDelivery
import java.net.URLEncoder

class PreSearchDelivery(internal var context : Context,
                        internal var iViewSearchDelivery: IViewSearchDelivery)
                : IPreSearchDelivery{
    val interactorSearchDelivery : InteractorSearchDelivery

    init {
        interactorSearchDelivery = InteractorSearchDelivery(context , this)
    }

    fun searchPlace(address : String){
        val addressEncode = URLEncoder.encode(address , "UTF8")
        interactorSearchDelivery.searchPlace(addressEncode)
    }

    override fun successSearchPlace(arrPlace: ArrayList<Place>) {
        iViewSearchDelivery.successSearchPlace(arrPlace)
    }

    override fun failureSearchPlace(message: String) {
        iViewSearchDelivery.failureSearchPlace(message)
    }

}