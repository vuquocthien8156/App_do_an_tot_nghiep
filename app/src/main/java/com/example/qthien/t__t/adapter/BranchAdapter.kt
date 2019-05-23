package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.week3_ryder.GlideApp
import kotlinx.android.synthetic.main.item_recy_branch.view.*

class BranchAdapter(var context : Context, var arrBranch : ArrayList<String>)
    : RecyclerView.Adapter<BranchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_branch , p0 , false))

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        GlideApp.with(context)
            .load("https://noithatcaphe.vn/images/2016/06/14/thiet-ke-noi-that-quan-cafe-phong-cach-co-dien-dep-doc-dao-2.jpg")
            .into(p0.img)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.imgBranch
    }
}