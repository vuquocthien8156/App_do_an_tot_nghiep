package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import kotlinx.android.synthetic.main.item_recy_root_branch.view.*

class BranchRootAdapter(var context : Context, var arrBranch : ArrayList<String>)
    : RecyclerView.Adapter<BranchRootAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_root_branch , p0 , false))

    override fun getItemCount(): Int = 7

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val layoutManager = LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL , false)
        val adapter = BranchAdapter(context , arrayListOf())
        p0.recy.layoutManager = layoutManager
        p0.recy.adapter = adapter
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recy = itemView.recylerRootBranch
    }
}