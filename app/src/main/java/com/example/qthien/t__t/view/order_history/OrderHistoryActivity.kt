package com.example.qthien.t__t.view.order_history

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.OrderHistoryAdapter
import kotlinx.android.synthetic.main.activity_order_history.*

class OrderHistoryActivity : AppCompatActivity() {

    lateinit var adapter : OrderHistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        adapter = OrderHistoryAdapter(this , arrayListOf())
        recy_order_history.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recy_order_history.adapter = adapter
    }
}
