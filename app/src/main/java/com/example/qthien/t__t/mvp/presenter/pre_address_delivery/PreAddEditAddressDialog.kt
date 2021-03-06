package com.example.qthien.t__t.mvp.presenter.pre_address_delivery

import com.example.qthien.t__t.mvp.interactor.InAddEditAddress
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.mvp.view.delivery_address.IDialogEditCreateAddress

class PreAddEditAddressDialog(var iDialogEditCreateAddress: IDialogEditCreateAddress)
    : IPreAddEditAddressDialog {

    fun addAddressInfo(infoAddress : InfoAddress){
        InAddEditAddress(this).addAddressInfo(infoAddress)
    }

    fun updateAddressInfo(infoAddress : InfoAddress){
        InAddEditAddress(this).updateAddressInfo(infoAddress)
    }

    override fun failureEditCreateAddress(message: String) {
        iDialogEditCreateAddress.failureEditCreateAddress(message)
    }

    override fun resultEditOrCreateAddress(status: String?) {
        iDialogEditCreateAddress.resultEditOrCreateAddress(status)
    }
}