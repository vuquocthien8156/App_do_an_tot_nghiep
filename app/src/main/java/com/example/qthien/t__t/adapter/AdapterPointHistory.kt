package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Point
import com.example.qthien.t__t.mvp.view.order.OrderHistoryActivity
import kotlinx.android.synthetic.main.item_recy_point.view.*
import java.text.SimpleDateFormat
import java.util.*

class AdapterPointHistory(var context : Context, var arrPoint : ArrayList<Point>)
    : RecyclerView.Adapter<AdapterPointHistory.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterPointHistory.ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_point , p0 , false))

    override fun getItemCount(): Int = arrPoint.size

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(p0: AdapterPointHistory.ViewHolder, p1: Int) {
        val p = arrPoint.get(p1)
        if(p.type == 1){
            p0.txtPoint.setBackgroundResource(R.color.colorAccent)
            p0.txtPoint.setText("+${p.point}")
        }
        else{
            p0.txtPoint.setBackgroundResource(R.color.colorPrimary)
            p0.txtPoint.setText("-${p.point}")
        }

        p0.txtIdOrder.setText(context.getString(R.string.orderr) + " " + p.idOrderText)

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        format.setTimeZone(TimeZone.getTimeZone("GMT"))
        val format1 = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val timeFormat = format1.format(format.parse(p.time))
        p0.txtTime.setText(timeFormat)

        p0.txtWatch.setOnClickListener({
            val i = Intent(context , OrderHistoryActivity::class.java)
            i.putExtra("id_order" , p.idOrder)
            context.startActivity(i)
        })
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtPoint = itemView.txtPoint
        val txtIdOrder = itemView.txtOrderId
        val txtTime = itemView.txtTime
        val txtWatch = itemView.txtWatch
    }
}