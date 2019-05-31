package com.example.qthien.t__t.view.order_history

import com.example.qthien.t__t.model.Order

interface IOrderHistory {
    fun resultgGetOrderByAccount(arrOrder : ArrayList<Order>?)
    fun failureOrderHistory(message : String)
}