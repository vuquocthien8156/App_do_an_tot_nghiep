package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Order
import com.example.qthien.t__t.view.order_history.OrderHistoryDetailActivity
import kotlinx.android.synthetic.main.item_recy_history_order.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class OrderHistoryAdapter(internal var context : Context
                          , internal var arrNews : ArrayList<Order>) :
        RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_history_order , p0 , false))

    override fun getItemCount(): Int = arrNews.size

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
        val o = arrNews[p1]

        val infoCus = o.infoOrder.split("-")
        vh.txtNameCus.text = infoCus.get(0)
        vh.txtPhone.text = infoCus.get(1)
        vh.txtAddress.text = infoCus.get(2)

        vh.txtTotal.text = DecimalFormat("###,###,###").format(o.priceTotal) + " đ"

        val statusNow = o.listStatus.maxBy { it.timeStatus }
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val format1 = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val timeFormat = format1.format(format.parse(statusNow?.timeStatus))
        vh.txtTime.text = timeFormat
        vh.txtStatus.text = statusNow?.nameStatus

        vh.txtPoint.text = "${o.priceTotal/10000}"

        vh.relaLayoutItemOrder.setOnClickListener({
            val i = Intent(context , OrderHistoryDetailActivity::class.java)
            i.putExtra("idOrder" , o)
            context.startActivity(i)
        })
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val relaLayoutItemOrder = itemView.relaLayoutItemOrder
        val txtNameCus = itemView.txtNameCustomerOrder
        val txtAddress = itemView.txtAddressOrder
        val txtStatus = itemView.txtStatusOrder
        val txtTime = itemView.txtTimeOrder
        val txtPoint = itemView.txtPointOrder
        val txtTotal = itemView.txtTotal
        val txtPhone = itemView.txtPhoneCustomerOrder
    }
}