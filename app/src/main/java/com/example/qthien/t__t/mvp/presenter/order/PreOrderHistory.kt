package com.example.qthien.t__t.mvp.presenter.order

import com.example.qthien.t__t.mvp.interactor.InOrder
import com.example.qthien.t__t.model.Order
import com.example.qthien.t__t.mvp.view.order.IOrderHistory

class PreOrderHistory(var iOrderHistory: IOrderHistory) : IPreOrderHistory {

    fun getOrderByUser(id : Int){
        InOrder(this).getOrderByUser(id)
    }

    override fun resultgGetOrderByAccount(arrOrder: ArrayList<Order>?) {
        iOrderHistory.resultgGetOrderByAccount(arrOrder)
    }

    override fun failureOrderHistory(message: String) {
        iOrderHistory.failureOrderHistory(message)
    }
}