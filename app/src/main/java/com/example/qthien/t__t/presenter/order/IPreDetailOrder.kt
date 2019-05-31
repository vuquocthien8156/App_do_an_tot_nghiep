package com.example.qthien.t__t.presenter.order

import com.example.qthien.t__t.model.DetailOrder

interface IPreDetailOrder {
    fun resultGetOrderDetail(arrDetail : ArrayList<DetailOrder>?)
    fun failureOrderDetail(message : String)
}