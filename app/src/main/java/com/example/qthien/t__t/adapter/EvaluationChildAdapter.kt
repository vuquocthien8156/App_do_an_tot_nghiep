package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.ChildEvaluation
import com.example.qthien.t__t.mvp.view.main.MainActivity
import kotlinx.android.synthetic.main.item_reply_child.view.*
import java.text.SimpleDateFormat

class EvaluationChildAdapter(var context: Context,
                             var arrEvaluation: ArrayList<ChildEvaluation>)
    : RecyclerView.Adapter<EvaluationChildAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder
    = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reply_child , p0 , false))

    override fun getItemCount(): Int = arrEvaluation.size

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val child = arrEvaluation.get(p1)
        val sim = SimpleDateFormat("dd/mm/yyyy")
        var nameAndTime = child.nameCustomer + " - " + sim.format(SimpleDateFormat("yyyy-mm-dd").parse(child.time))
        if(MainActivity.customer!!.idCustomer == child.idAccount)
            nameAndTime = child.nameCustomer + " (${(context.getString(R.string.me))})" +
                    " - " + sim.format(SimpleDateFormat("yyyy-mm-dd").parse(child.time))

        p0.txtNameAndTime.setText(nameAndTime)
        p0.txtContent.setText(child.content)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtNameAndTime = itemView.txtNameUserAndTime
        val txtContent = itemView.txtContentValuationChild
    }
}