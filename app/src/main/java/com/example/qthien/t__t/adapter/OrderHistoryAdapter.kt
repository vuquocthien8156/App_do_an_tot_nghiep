package com.example.qthien.t__t.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.view.order_history.OrderHistoryDetailActivity
import kotlinx.android.synthetic.main.item_recy_history_order.view.*

class OrderHistoryAdapter(internal var context : Context
                          , internal var arrNews : ArrayList<String>) :
        RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_history_order , p0 , false))

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
        vh.relaLayoutItemOrder.setOnClickListener({
            context.startActivity(Intent(context , OrderHistoryDetailActivity::class.java))
        })
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val relaLayoutItemOrder = itemView.relaLayoutItemOrder
    }
}