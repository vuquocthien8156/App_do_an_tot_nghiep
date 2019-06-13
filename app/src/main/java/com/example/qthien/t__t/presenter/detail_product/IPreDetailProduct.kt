package com.example.qthien.t__t.presenter.detail_product

import com.example.qthien.t__t.model.ResponseEvaluation

interface IPreDetailProduct {
    fun failureCallDetail(message : String)
    fun successAddTks(status: String?)
    fun successCallDetail(response : ResponseEvaluation)
}