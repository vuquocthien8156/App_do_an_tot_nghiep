package com.example.qthien.t__t.presenter.pre_search_product

import com.example.qthien.t__t.interactor.InSearchProductActivity
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.view.search_product.IViewSearchProductActivity

class PreSearchProductActivity(var iViewSearchProductActivity: IViewSearchProductActivity)
    : IPreSearchProductActivity{

    val interactor = InSearchProductActivity(this)
    fun getAllProduct(){
        interactor.getAllProduct()
    }

    override fun failure(message: String) {
        iViewSearchProductActivity.failure(message)
    }

    override fun resultGetAllProduct(arrResult: ArrayList<Product>?) {
        iViewSearchProductActivity.resultGetAllProduct(arrResult)
    }
}