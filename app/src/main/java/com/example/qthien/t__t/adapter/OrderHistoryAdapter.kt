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
import com.example.qthien.t__t.mvp.view.order.OrderHistoryDetailActivity
import kotlinx.android.synthetic.main.item_recy_history_order.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryAdapter(internal var context : Context
                          , internal var arrNews : ArrayList<Order>) :
        RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_history_order , p0 , false))

    override fun getItemCount(): Int = arrNews.size

    var idOrderSelected = 0

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
        val o = arrNews[p1]

        if(idOrderSelected != 0 && idOrderSelected == o.idOrder)
            vh.relaLayoutItemOrder.setBackgroundResource(R.color.colorPrimaryLight)
        else
            vh.relaLayoutItemOrder.setBackgroundResource(R.color.color_grey_light)

        val infoCus = o.infoOrder.split("-")
        vh.txtNameCus.text = infoCus.get(0)
        vh.txtPhone.text = infoCus.get(1)
        vh.txtAddress.text = infoCus.get(2)
        var price = o.priceTotal + o.priceTranfrom - o.priceTotalDiscount
        if( (o.paymentMethod / 10) == 2 || o.paymentMethod == 2)
            price -= (o.point * 1000)

        vh.txtTotal.text = DecimalFormat("###,###,###").format(price) + " đ"

        val statusNow = o.listStatus.maxBy { it.timeStatus }
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        format.setTimeZone(TimeZone.getTimeZone("GMT"))
        val format1 = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val timeFormat = format1.format(format.parse(statusNow?.timeStatus))
        vh.txtTime.text = timeFormat
        vh.txtStatus.text = statusNow?.nameStatus

        val method = o.paymentMethod / 10
        if(o.paymentMethod == 2 || method == 2)
            vh.txtPoint.text = context.getString(R.string.use) + " ${o.point}"
        else
            vh.txtPoint.text = context.getString(R.string.accumulated) + " ${o.point}"

        vh.relaLayoutItemOrder.setOnClickListener({
            val i = Intent(context , OrderHistoryDetailActivity::class.java)
            i.putExtra("idOrder" , o)
            context.startActivity(i)
        })
    }

    fun setidOrderSelected(id : Int){
        idOrderSelected = id
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