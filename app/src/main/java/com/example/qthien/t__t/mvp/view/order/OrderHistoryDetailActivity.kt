package com.example.qthien.t__t.mvp.view.order

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.DetailOrderAdapter
import com.example.qthien.t__t.adapter.StatusAdapter
import com.example.qthien.t__t.model.DetailOrder
import com.example.qthien.t__t.model.Order
import com.example.qthien.t__t.model.StatusOrder
import com.example.qthien.t__t.mvp.presenter.order.PreDetailOrderHistory
import kotlinx.android.synthetic.main.activity_order_history_detail.*
import java.text.DecimalFormat

class OrderHistoryDetailActivity : AppCompatActivity(), IOrderDetailHistory {

    @SuppressLint("SetTextI18n")
    override fun resultGetOrderDetail(arrDetail: ArrayList<DetailOrder>?) {
        if (arrDetail != null) {
            Log.d("DetaillOrder", arrDetail.toString())
            arrDetailOrder.addAll(arrDetail)
            adapterDetailOrder.notifyDataSetChanged()

            val d = DecimalFormat("###,###,###")
            txtTotalProduct.text = d.format(order?.priceTotal) + " đ"
            txtTotalTransportFee.text = d.format(order!!.priceTranfrom) + " đ"

            var totalSum = order!!.priceTotal + order!!.priceTranfrom - order!!.priceTotalDiscount
            if(order?.paymentMethod?.div(10) == 2 || order?.paymentMethod == 2)
                totalSum -= order!!.point  * 1000

            txtTotalSum.text = d.format(totalSum) + " đ"

            txtCountProduct.text = "( ${arrDetail.size} ${getString(R.string.product)} )"

            progressLoader.visibility = View.GONE
            relaDetailOrder.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun failureOrderDetail(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    lateinit var adapterStatus: StatusAdapter
    lateinit var arrStatus: ArrayList<StatusOrder>
    lateinit var arrDetailOrder: ArrayList<DetailOrder>

    lateinit var adapterDetailOrder: DetailOrderAdapter

    var order: Order? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history_detail)

        setSupportActionBar(toolbarOrder)
        toolbarOrder.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        order = intent.extras?.getParcelable("idOrder")

        supportActionBar?.setTitle("${getString(R.string.orderr)} ${order?.idOrderText}")

        arrStatus = ArrayList()
        arrDetailOrder = ArrayList()

        if (order != null) {
            PreDetailOrderHistory(this).getOrderDetail(order!!.idOrder)
            arrStatus.addAll(order!!.listStatus.sortedByDescending { it.timeStatus })

            val info = order?.infoOrder?.split("-")
            txtNameCustomerDetailOrder.text = info?.get(0)
            txtPhoneDetailOrder.text = info?.get(1)
            txtAddressDetailOrder.text = info?.get(2)

            val methodAfter = order?.paymentMethod!!%10
            val methodBefore = order?.paymentMethod!!/10
            if (order?.paymentMethod == 2 || methodBefore == 2) {
                txtPoint.setText(order?.point.toString())
                relaPointUse.visibility = View.VISIBLE
                txtMethodDepay.visibility = View.VISIBLE
            }

            if(order?.paymentMethod == 1 || methodAfter == 1)
                txtMethodDepay.visibility = View.VISIBLE

            if(order?.paymentMethod == 3 || methodAfter == 3)
                txtMethodDepayCard.visibility = View.VISIBLE

            if(order?.noteOrder.equals("") || order?.noteOrder == null)
                relaNote.visibility = View.GONE
            else{
                relaNote.visibility = View.VISIBLE
                txtNote.setText(order?.noteOrder)
            }

            if(order?.priceTotalDiscount != 0L){
                txtPriceDiscount.setText(DecimalFormat("###,###,###").format(order?.priceTotalDiscount) + " đ")
            }
            else
                relaDiscount.visibility = View.GONE
        }

        adapterStatus = StatusAdapter(this, arrStatus)
        recylerStatusOrder.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recylerStatusOrder.adapter = adapterStatus

        adapterDetailOrder = DetailOrderAdapter(this, arrDetailOrder)
        recy_detail_order.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recy_detail_order.adapter = adapterDetailOrder


        val method = order!!.paymentMethod / 10
        if(method == 2)
            txtMethodDepayPoint.visibility = View.VISIBLE
        else
            txtMethodDepayPoint.visibility = View.GONE

        val methodDiferenceInt = order!!.paymentMethod % 10

        if(methodDiferenceInt == 1) {
            txtMethodDepay.visibility = View.VISIBLE
            txtMethodDepayCard.visibility = View.GONE
        }else {
            txtMethodDepayCard.visibility = View.VISIBLE
            txtMethodDepay.visibility = View.GONE
        }
    }
}
