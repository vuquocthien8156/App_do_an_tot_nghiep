package com.example.qthien.t__t.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.view.reply_ealuation.DetailEvaluationActivity
import kotlinx.android.synthetic.main.item_recycler_evaluation.view.*

class EvaluationAdapter(var context : Context, arrEvaluation : ArrayList<String>) : RecyclerView.Adapter<EvaluationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_evaluation , p0 , false))

    override fun getItemCount(): Int = 2

    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
        vh.recyclerChild.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        vh.recyclerChild.adapter = EvaluationChildAdapter(context , arrayListOf())

        vh.txtReply.setOnClickListener({
            context.startActivity(Intent(context , DetailEvaluationActivity::class.java))
        })
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyclerChild = itemView.recyclerReply
        val txtReply = itemView.txtReply
    }
}