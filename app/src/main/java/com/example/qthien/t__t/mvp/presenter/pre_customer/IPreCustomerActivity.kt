package com.example.qthien.t__t.mvp.presenter.pre_customer

interface IPreCustomerActivity {
    fun failure(message : String)
    fun resultUpdateUser(status: String?)
    fun resultUploadImage(fileName: String?)
}