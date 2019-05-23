package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R

class EvaluationChildAdapter(var context : Context,
                             var arrEvaluation : ArrayList<String> )
    : RecyclerView.Adapter<EvaluationChildAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder
    = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reply_child , p0 , false))

    override fun getItemCount(): Int = 2
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
}