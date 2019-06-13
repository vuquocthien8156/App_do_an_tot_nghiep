package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.ImagesSelected
import kotlinx.android.synthetic.main.item_recy_image.view.*

class ImagesAdapter(var context : Context ,var  arrImage : ArrayList<ImagesSelected>)
    : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    interface ImagesAdapterCallActi{
        fun selectedImage(url : String)
        fun unSelectedImage(url : String)
    }

    private var imagesAdapterCallActi : ImagesAdapterCallActi? = null
    private var isFull = false

    init {
        if(context is ImagesAdapterCallActi)
            imagesAdapterCallActi = context as ImagesAdapterCallActi
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_image , p0 , false))
    }

    override fun getItemCount(): Int = arrImage.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val r = arrImage[p1]
        GlideApp.with(context).load(r.url).into(p0.img)

        if(r.selected){
            p0.lnChecked.visibility = View.VISIBLE
            imagesAdapterCallActi?.selectedImage(r.url)
        }
        else{
            p0.lnChecked.visibility = View.GONE
            imagesAdapterCallActi?.unSelectedImage(r.url)
        }


        p0.frmLayout.setOnClickListener({
            if (!r.selected) {
                if(!isFull) {
                    p0.lnChecked.visibility = View.VISIBLE
                    imagesAdapterCallActi?.selectedImage(r.url)
                    r.selected = !r.selected
                }
            }
            else {
                p0.lnChecked.visibility = View.GONE
                imagesAdapterCallActi?.unSelectedImage(r.url)
                r.selected = !r.selected
            }
        })
    }

    fun setFulll(boolean: Boolean){
        isFull = boolean
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.img
        val frmLayout = itemView.frmLayout
        val lnChecked = itemView.lnChecked
    }
}