package com.example.qthien.t__t.mvp.presenter.order

import com.example.qthien.t__t.model.DetailOrder

interface IPreDetailOrderHistory {
    fun resultGetOrderDetail(arrDetail : ArrayList<DetailOrder>?)
    fun failureOrderDetail(message : String)
}