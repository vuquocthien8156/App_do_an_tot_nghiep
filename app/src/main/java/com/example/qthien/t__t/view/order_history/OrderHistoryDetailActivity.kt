package com.example.qthien.t__t.view.order_history

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.DetailOrderAdapter
import com.example.qthien.t__t.adapter.StatusAdapter
import com.example.qthien.t__t.model.Status
import kotlinx.android.synthetic.main.activity_order_history_detail.*

class OrderHistoryDetailActivity : AppCompatActivity() {

    lateinit var adapterStatus : StatusAdapter
    lateinit var arrStatus : ArrayList<Status>

    lateinit var adapterDetailOrder : DetailOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history_detail)

        arrStatus = ArrayList()
        arrStatus.add(Status("Đã hoàn thành" , "12:06"))
        arrStatus.add(Status("Đang vận chuyển" , "11:48"))
        arrStatus.add(Status("Chuẩn bị thức uống" , "11:33"))
        arrStatus.add(Status("Đã xác nhận" , "11:33"))
        arrStatus.add(Status("Nhận đơn hàng" , "11:32"))
        adapterStatus = StatusAdapter(this ,  arrStatus)
        recylerStatusOrder.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recylerStatusOrder.adapter = adapterStatus

        adapterDetailOrder = DetailOrderAdapter(this , arrayListOf())
        recy_detail_order.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recy_detail_order.adapter = adapterDetailOrder
    }
}
