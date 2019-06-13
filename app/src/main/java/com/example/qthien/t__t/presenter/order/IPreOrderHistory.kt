package com.example.qthien.t__t.presenter.order

import com.example.qthien.t__t.model.Order

interface IPreOrderHistory {
    fun resultgGetOrderByAccount(arrOrder : ArrayList<Order>?)
    fun failureOrderHistory(message : String)
}