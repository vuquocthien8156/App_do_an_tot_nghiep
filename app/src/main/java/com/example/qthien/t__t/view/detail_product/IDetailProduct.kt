package com.example.qthien.t__t.view.detail_product

import com.example.qthien.t__t.model.ResponseEvaluation

interface IDetailProduct {
    fun failureCallDetail(message : String)
    fun successAddTks(status: String?)
    fun successCallDetail(response : ResponseEvaluation)
}