package com.example.qthien.t__t.mvp.presenter.pre_customer

import com.example.qthien.t__t.mvp.interactor.InCustomerActivity
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.mvp.view.customer.IViewCustomerActivity
import java.io.File

class PreCustomerActivity(var iViewCustomerActivity: IViewCustomerActivity) : IPreCustomerActivity {
    override fun resultUploadImage(fileName: String?) {
        iViewCustomerActivity.resultUploadImage(fileName)
    }

    override fun failure(message: String) {
        iViewCustomerActivity.failure(message)
    }

    override fun resultUpdateUser(status: String?) {
        iViewCustomerActivity.resultUpdateUser(status)
    }

    val inter = InCustomerActivity(this)

    // function
    fun uploadImage(fileImage : File?){
        inter.uploadImage(fileImage)
    }

    fun uploadInfoCustomer(customer : Customer){
        if(customer.birthday != null)
        {
            val birthday = customer.birthday!!.split("/")
            customer.birthday = "${birthday[2]}/${birthday[1]}/${birthday[0]}"
        }
        inter.uploadInfoCustomer(customer)
    }
}