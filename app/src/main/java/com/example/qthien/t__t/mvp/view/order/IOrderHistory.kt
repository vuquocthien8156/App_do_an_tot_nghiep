package com.example.qthien.t__t.mvp.view.order

import com.example.qthien.t__t.model.Order

interface IOrderHistory {
    fun resultgGetOrderByAccount(arrOrder : ArrayList<Order>?)
    fun failureOrderHistory(message : String)
}