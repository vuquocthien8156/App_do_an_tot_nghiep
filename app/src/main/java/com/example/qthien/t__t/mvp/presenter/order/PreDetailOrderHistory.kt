package com.example.qthien.t__t.mvp.presenter.order

import com.example.qthien.t__t.mvp.interactor.InOrderDetail
import com.example.qthien.t__t.model.DetailOrder
import com.example.qthien.t__t.mvp.view.order.IOrderDetailHistory

class PreDetailOrderHistory(var iOrderDetail : IOrderDetailHistory) : IPreDetailOrderHistory {

    fun getOrderDetail(idOrder : Int){
        InOrderDetail(this).getOrderDetail(idOrder)
    }

    override fun resultGetOrderDetail(arrDetail: ArrayList<DetailOrder>?) {
        iOrderDetail.resultGetOrderDetail(arrDetail)
    }

    override fun failureOrderDetail(message: String) {
        iOrderDetail.failureOrderDetail(message)
    }
}