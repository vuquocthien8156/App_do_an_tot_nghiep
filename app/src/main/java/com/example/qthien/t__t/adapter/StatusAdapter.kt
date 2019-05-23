package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Status
import kotlinx.android.synthetic.main.item_recy_status.view.*

class StatusAdapter(var context: Context, var arrStatus: ArrayList<Status>)
    : RecyclerView.Adapter<StatusAdapter.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_status , p0 , false))

    override fun getItemCount(): Int = arrStatus.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtName.setText(arrStatus[p1].nameStatus)
        p0.txtTime.setText(arrStatus[p1].timeStatus)
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtTime = itemView.txtTimeStatus
        val txtName = itemView.txtNameStatus
    }
}