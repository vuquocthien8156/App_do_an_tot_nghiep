package com.example.qthien.t__t.view.customer

interface IViewCustomerActivity {
    fun failure(message : String)
    fun resultUpdateUser(status: String?)
    fun resultUploadImage(fileName : String?)
}