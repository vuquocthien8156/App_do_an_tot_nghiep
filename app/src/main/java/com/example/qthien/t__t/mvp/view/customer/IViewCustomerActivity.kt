package com.example.qthien.t__t.mvp.view.customer

interface IViewCustomerActivity {
    fun failure(message : String)
    fun resultUpdateUser(status: String?)
    fun resultUploadImage(fileName : String?)
}