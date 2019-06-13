package com.example.qthien.t__t.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.News
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.example.qthien.t__t.view.WebActivity
import kotlinx.android.synthetic.main.item_recyler_news.view.*

class NewsAdapter(internal var context : Context
                  , internal var arrNews : ArrayList<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recyler_news , p0 , false))

    override fun getItemCount(): Int = arrNews.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val n = arrNews.get(p1)
        GlideApp.with(context).load("${RetrofitInstance.baseUrl}/${n.imageNews}").into(p0.img)
        p0.txtName.setText(n.nameNews)
        p0.txtContent.setText(n.contentNews)
        p0.layout.setOnClickListener({
            val url = "${RetrofitInstance.baseUrl}/products/news?id=${n.idNews}"
            val i = Intent(context , WebActivity::class.java)
            i.putExtra("url" , url)
            i.putExtra("title" , n.nameNews)
            context.startActivity(i)
        })
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.imgNews
        val txtName = itemView.txtNameNews
        val txtContent = itemView.txtContent
        val layout = itemView.layout
    }
}