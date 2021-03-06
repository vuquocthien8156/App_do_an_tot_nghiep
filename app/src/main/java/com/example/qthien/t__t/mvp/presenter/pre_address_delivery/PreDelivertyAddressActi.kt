package com.example.qthien.t__t.mvp.presenter.pre_address_delivery

import com.example.qthien.t__t.mvp.interactor.InDeliveryAddressActi
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.mvp.view.delivery_address.IDeliveryAddressActi

class PreDelivertyAddressActi(var iDeliveryAddress : IDeliveryAddressActi) : IPreDeliveryAddressActi {

    fun getAllAddressInfoUser(idAccount : Int){
        InDeliveryAddressActi(this).getAllAddressInfoUser(idAccount)
    }

    override fun resultGetAllAddressInfoUser(arrrAddressInfo: ArrayList<InfoAddress>) {
        iDeliveryAddress.resultGetAllAddressInfoUser(arrrAddressInfo)
    }

    override fun failureDeliveryAddress(message: String) {
        iDeliveryAddress.failureDeliveryAddress(message)
    }
}