package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import kotlinx.android.synthetic.main.item_recy_images_child.view.*

class ImageSmallAdapter(var context : Context, var arrUrl : ArrayList<String>):
    RecyclerView.Adapter<ImageSmallAdapter.ViewHolder>(){

    interface ImagesAdapterCommunication{
        fun itemRecyclerSelected(position: Int)
    }

    var positionSelected = -1
    var imagesAdapterCommunication : ImagesAdapterCommunication? = null

    init {
        if(context is ImagesAdapterCommunication)
            imagesAdapterCommunication = context as ImagesAdapterCommunication
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_images_child , p0 , false))

    override fun getItemCount(): Int = arrUrl.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        GlideApp.with(context).load("${RetrofitInstance.baseUrl}/${arrUrl[p1]}").into(p0.img)

        p0.cardItem.setOnClickListener({
            imagesAdapterCommunication?.itemRecyclerSelected(p1)
        })

        if(positionSelected != -1){
            if(p1 == positionSelected)
                p0.cardItem.setBackgroundResource(R.drawable.shape_item_img_selected)
            else
                p0.cardItem.setBackgroundResource(R.drawable.shape_item_img_un_selected)
        }
    }

    fun setSelectedItem(position : Int){
        positionSelected = position
        notifyDataSetChanged()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.img
        val cardItem = itemView.cardItem
    }
}