package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.view.add_to_cart.AddToCartActivity
import com.example.qthien.t__t.view.detail_product.DetailProductActivity
import com.example.qthien.week3_ryder.GlideApp
import kotlinx.android.synthetic.main.item_product.view.*

class ProductsAdapter(internal var context : Context,
                      internal var arrProducts : ArrayList<String>) :
                            RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder
        = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product , p0 , false))

    override fun getItemCount(): Int = 15

    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
        Log.d("Positionnna" , "${p1 + 1}")
        GlideApp.with(context)
            .load("https://product.hstatic.net/1000075078/product/tra_dao_cam_sa_on_bg_master.jpg")
            .into(vh.imgProducts)

        vh.layoutContain.setOnClickListener {
            context.startActivity(Intent(context , DetailProductActivity::class.java))
        }

        vh.imgAdd.setOnClickListener {
            context.startActivity(Intent(context , AddToCartActivity::class.java))
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imgProducts = itemView.imgProduct
        var imgAdd = itemView.imgAdd
        var layoutContain = itemView.relaContainProduct
    }
}