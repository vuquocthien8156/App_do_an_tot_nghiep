package com.example.qthien.t__t.mvp.presenter.pre_address_delivery

import com.example.qthien.t__t.model.InfoAddress

interface IPreDeliveryAddressActi {
    fun failureDeliveryAddress(message : String)
    fun resultGetAllAddressInfoUser(arrrAddressInfo : ArrayList<InfoAddress>)
}