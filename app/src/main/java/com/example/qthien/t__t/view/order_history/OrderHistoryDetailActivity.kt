package com.example.qthien.t__t.view.order_history

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.DetailOrderAdapter
import com.example.qthien.t__t.adapter.StatusAdapter
import com.example.qthien.t__t.model.DetailOrder
import com.example.qthien.t__t.model.Order
import com.example.qthien.t__t.model.StatusOrder
import com.example.qthien.t__t.presenter.order.PreDetailOrder
import kotlinx.android.synthetic.main.activity_order_history_detail.*
import java.text.DecimalFormat

class OrderHistoryDetailActivity : AppCompatActivity() , IOrderDetailHistory {

    @SuppressLint("SetTextI18n")
    override fun resultGetOrderDetail(arrDetail: ArrayList<DetailOrder>?) {
        if(arrDetail != null){
            Log.d("DetaillOrder" , arrDetail.toString())
            arrDetailOrder.addAll(arrDetail)
            adapterDetailOrder.notifyDataSetChanged()

            val d = DecimalFormat("###,###,###")
            txtTotalProduct.text = d.format(arrDetailOrder.sumBy { (it.total*it.quantity).toInt() }) + " đ"
            txtTotalTransportFee.text = d.format(order!!.priceTranfrom)  + " đ"
            txtTotalSum.text = d.format(order!!.priceTotal)  + " đ"

            var quantity = 0

            for(i in arrDetailOrder){
                quantity += (1 + (i.arrTopping?.sumBy { it.quantity } ?: 0))
            }
            txtCountProduct.text = "( $quantity ${getString(R.string.product)} )"
        }
    }

    override fun failureOrderDetail(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    lateinit var adapterStatus : StatusAdapter
    lateinit var arrStatus : ArrayList<StatusOrder>
    lateinit var arrDetailOrder: ArrayList<DetailOrder>

    lateinit var adapterDetailOrder : DetailOrderAdapter

    var order : Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history_detail)

        setSupportActionBar(toolbarOrder)
        toolbarOrder.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        order = intent.extras?.getParcelable("idOrder")

        supportActionBar?.setTitle("${getString(R.string.orderr)} ${order?.idOrder}")

        arrStatus = ArrayList()
        arrDetailOrder = ArrayList()

        if(order != null) {
            PreDetailOrder(this).getOrderDetail(order!!.idOrder)
            arrStatus.addAll(order!!.listStatus.sortedByDescending{ it.timeStatus })

            val info = order?.infoOrder?.split("-")
            txtNameCustomerDetailOrder.text = info?.get(0)
            txtPhoneDetailOrder.text = info?.get(1)
            txtAddressDetailOrder.text = info?.get(2)
        }

        adapterStatus = StatusAdapter(this ,  arrStatus)
        recylerStatusOrder.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recylerStatusOrder.adapter = adapterStatus

        adapterDetailOrder = DetailOrderAdapter(this , arrDetailOrder)
        recy_detail_order.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recy_detail_order.adapter = adapterDetailOrder
    }
}
