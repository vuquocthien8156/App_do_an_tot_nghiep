package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.StatusOrder
import kotlinx.android.synthetic.main.item_recy_status.view.*
import java.text.SimpleDateFormat

class StatusAdapter(var context: Context, var arrStatus: ArrayList<StatusOrder>)
    : RecyclerView.Adapter<StatusAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_status , p0 , false))

    override fun getItemCount(): Int = arrStatus.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtName.setText(arrStatus[p1].nameStatus)

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val format1 = SimpleDateFormat("HH:mm")
        val timeFormat = format1.format(format.parse(arrStatus[p1].timeStatus))
        p0.txtTime.setText(timeFormat)
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtTime = itemView.txtTimeStatus
        val txtName = itemView.txtNameStatus
    }
}