package com.example.qthien.t__t.mvp.view.delivery_address

import com.example.qthien.t__t.model.InfoAddress

interface IDeliveryAddressActi {
    fun failureDeliveryAddress(message : String)
    fun resultGetAllAddressInfoUser(arrrAddressInfo : ArrayList<InfoAddress>)
}