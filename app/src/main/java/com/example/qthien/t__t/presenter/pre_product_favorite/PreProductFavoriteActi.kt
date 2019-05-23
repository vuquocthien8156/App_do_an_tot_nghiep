package com.example.qthien.t__t.presenter.pre_product_favorite

import com.example.qthien.t__t.interactor.InProductFavoriteActi
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.view.product_favorite.IViewProductFavoriteActi

class PreProductFavoriteActi(var iViewProductFavoriteActi: IViewProductFavoriteActi)
        : IPreProductFavoriteActi {
    private val inter = InProductFavoriteActi(this)
    fun favoriteProduct(idProduct : Int , idUser: Int , like : Int) {
        inter.favoriteProduct(idProduct , idUser , like)
    }

    fun getProductFavorite(idUser : Int) {
        inter.getProductFavorite(idUser)
    }

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {
        iViewProductFavoriteActi.resultGetProductFavorite(arrResult)
    }

    override fun favoriteProduct(resultCode: String) {
        iViewProductFavoriteActi.favoriteProduct(resultCode)
    }

    override fun failure(message: String) {
        iViewProductFavoriteActi.failureFavorite(message)
    }
}