package com.example.qthien.t__t.mvp.presenter.pre_address_delivery

interface IPreAddEditAddressDialog {
    fun failureEditCreateAddress(message : String)
    fun resultEditOrCreateAddress(status: String?)
}