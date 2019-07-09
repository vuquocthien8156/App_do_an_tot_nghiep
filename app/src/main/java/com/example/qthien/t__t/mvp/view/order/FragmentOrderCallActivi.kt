package com.example.qthien.t__t.mvp.view.order

import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.model.OrderAdd

interface FragmentOrderCallActivi{
        fun addressValidation(info: String)
        fun depayToVerify(info: String ,orderAdd : OrderAdd , discount : Discount?)
        fun successAddOrder()
        fun hideLoader()
}