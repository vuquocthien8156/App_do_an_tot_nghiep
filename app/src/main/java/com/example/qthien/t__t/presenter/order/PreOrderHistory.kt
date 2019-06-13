package com.example.qthien.t__t.presenter.order

import com.example.qthien.t__t.interactor.InOrder
import com.example.qthien.t__t.model.Order
import com.example.qthien.t__t.view.order.IOrderHistory

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