package com.example.qthien.t__t.presenter.detail_product

import com.example.qthien.t__t.interactor.InDetailProduct
import com.example.qthien.t__t.model.ResponseEvaluation
import com.example.qthien.t__t.view.detail_product.IDetailProduct

class PreDetailProduct(var iDetailProduct: IDetailProduct) : IPreDetailProduct {
    override fun successAddTks(status: String?) {
        iDetailProduct.successAddTks(status)
    }

    override fun failureCallDetail(message: String) {
        iDetailProduct.failureCallDetail(message)
    }

    override fun successCallDetail(response: ResponseEvaluation) {
        iDetailProduct.successCallDetail(response)
    }

    fun callDetailOrEvaluation(idProduct : Int, idUser : Int , page : Int? ,  point : Int? , time : Int?){
        InDetailProduct(this).callDetailOrEvaluation(idProduct , idUser , page , point, time)
    }

    fun addTks(idEvaluate : Int , idCustomer : Int) {
        InDetailProduct(this).addTks(idEvaluate , idCustomer)
    }
}