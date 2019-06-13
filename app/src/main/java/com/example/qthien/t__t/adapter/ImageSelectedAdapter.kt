package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import kotlinx.android.synthetic.main.item_recy_image_selected.view.*

class ImageSelectedAdapter(var context : Context, var arrUrl : ArrayList<String>):
    RecyclerView.Adapter<ImageSelectedAdapter.ViewHolder>(){

    interface ImagesAdapterCommunication{
        fun removeImage(position: Int)
    }

    var imagesAdapterCommunication : ImagesAdapterCommunication? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        if(context is ImagesAdapterCommunication)
            imagesAdapterCommunication = context as ImagesAdapterCommunication
        return  ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_image_selected , p0 , false))
    }


    override fun getItemCount(): Int = arrUrl.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        GlideApp.with(context).load(arrUrl[p1]).into(p0.img)

        p0.ibtnRemove.setOnClickListener({
            Log.d("imagesAdapter" , imagesAdapterCommunication.toString())
            imagesAdapterCommunication?.removeImage(p1)
        })
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.imgSelected
        val ibtnRemove = itemView.ibtnRemove
    }
}