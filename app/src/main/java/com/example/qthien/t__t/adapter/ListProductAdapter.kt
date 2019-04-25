package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.R.id.recylerProduct
import kotlinx.android.synthetic.main.fragment_tab.*
import kotlinx.android.synthetic.main.item_list_product.view.*

class ListProductAdapter(internal var context : Context,
                         internal var arrListProduct : ArrayList<String>)
            : RecyclerView.Adapter<ListProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_product , p0 , false))

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
        val layoutManager = GridLayoutManager(context , 2)
        vh.recyclerView.layoutManager = layoutManager
        val adapter = ProductsAdapter(context , arrayListOf(""))
        vh.recyclerView.adapter = adapter
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var recyclerView = itemView.recyler_list_product
    }
}