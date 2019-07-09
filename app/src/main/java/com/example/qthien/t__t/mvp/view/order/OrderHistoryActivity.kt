package com.example.qthien.t__t.mvp.view.order

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.OrderHistoryAdapter
import com.example.qthien.t__t.model.Order
import com.example.qthien.t__t.mvp.presenter.order.PreOrderHistory
import com.example.qthien.t__t.mvp.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_order_history.*

class OrderHistoryActivity : AppCompatActivity() , IOrderHistory {

    override fun resultgGetOrderByAccount(arrOrder: ArrayList<Order>?) {
        if(arrOrder != null && arrOrder.size > 0) {
            this.arrOrder.clear()
            this.arrOrder.addAll(arrOrder)
            relaOrder.visibility = View.GONE
            recy_order_history.visibility = View.VISIBLE
        } else {
            relaOrder.visibility = View.VISIBLE
            recy_order_history.visibility = View.GONE
        }
        val idOrder = intent.extras?.getInt("id_order")
        if(idOrder != null && idOrder != 0) {
            adapter.setidOrderSelected(idOrder)
            adapter.notifyDataSetChanged()

            val index = arrOrder?.indexOfFirst { it.idOrder == idOrder } ?: 0
            recy_order_history.scrollToPosition(index)
        }
        else
            adapter.notifyDataSetChanged()

        swipeRefreshLayout.isRefreshing = false
    }

    override fun failureOrderHistory(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
        swipeRefreshLayout.isRefreshing = false
    }

    lateinit var adapter : OrderHistoryAdapter
    lateinit var arrOrder : ArrayList<Order>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        setSupportActionBar(toolbarOrder)
        toolbarOrder.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setTitle(R.string.order_history)

        arrOrder = ArrayList()

        adapter = OrderHistoryAdapter(this , arrOrder)
        recy_order_history.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recy_order_history.adapter = adapter

        PreOrderHistory(this).getOrderByUser(MainActivity.customer!!.idCustomer)

        swipeRefreshLayout.setOnRefreshListener {
            PreOrderHistory(this).getOrderByUser(MainActivity.customer!!.idCustomer)
        }

        swipeRefreshLayout.isRefreshing = true

        btnContinuteOrder.setOnClickListener({
            setResult(Activity.RESULT_OK , Intent())
            finish()
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
