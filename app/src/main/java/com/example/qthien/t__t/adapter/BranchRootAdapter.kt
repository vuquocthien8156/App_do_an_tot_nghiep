package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.BranchFolowArea
import kotlinx.android.synthetic.main.item_recy_root_branch.view.*

class BranchRootAdapter(var context : Context, var arrBranchArea : ArrayList<BranchFolowArea>)
    : RecyclerView.Adapter<BranchRootAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_root_branch , p0 , false))

    override fun getItemCount(): Int = arrBranchArea.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtName.setText(arrBranchArea[p1].nameArea)

        val layoutManager = LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL , false)
        val adapter = BranchAdapter(context , arrBranchArea[p1].arrBranch)
        p0.recy.layoutManager = layoutManager
        p0.recy.adapter = adapter
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recy = itemView.recylerRootBranch
        val txtName = itemView.txtNameAreaBranch
    }
}