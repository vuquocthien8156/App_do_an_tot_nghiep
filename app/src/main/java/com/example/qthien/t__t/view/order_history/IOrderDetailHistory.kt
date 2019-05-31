package com.example.qthien.t__t.view.order_history

import com.example.qthien.t__t.model.DetailOrder

interface IOrderDetailHistory {
    fun resultGetOrderDetail(arrDetail : ArrayList<DetailOrder>?)
    fun failureOrderDetail(message : String)
}