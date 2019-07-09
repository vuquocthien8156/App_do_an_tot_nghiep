package com.example.qthien.t__t.mvp.view.main

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.EndlessRecyclerViewScrollListener
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.NewsAdapter
import com.example.qthien.t__t.model.News
import com.example.qthien.t__t.mvp.presenter.pre_frag_news.PreFragNews
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity(), IViewFragNews {
    override fun failureNews(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun successGetNews(arrNews: ArrayList<News>) {
        if(arrNews.size > 0) {
            this.arrNews.addAll(arrNews)
            if(arrNews.size == 1)
                adapterNew.notifyDataSetChanged()
            else {
                adapterNew.notifyItemRangeInserted(adapterNew.itemCount, arrNews.size - 1)
            }
        }
        swipeRefreshLayout.isRefreshing = false
        progressLoader.visibility = View.GONE
    }

    lateinit var arrNews : ArrayList<News>
    lateinit var adapterNew : NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.news)
        setContentView(R.layout.activity_news)

        setSupportActionBar(toolbarNewsActi)
        toolbarNewsActi.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        arrNews = ArrayList()
        val layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyNewsActi.layoutManager = layoutManager
        adapterNew = NewsAdapter(this , arrNews)
        recyNewsActi.adapter = adapterNew
        recyNewsActi.isNestedScrollingEnabled = false

        PreFragNews(this@NewsActivity).getNews(1)

        val scroll = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.d("pagess" , page.toString())
                progressLoader.visibility = View.VISIBLE
                PreFragNews(this@NewsActivity).getNews(page+1)
            }
        }

        swipeRefreshLayout.isRefreshing = true
        swipeRefreshLayout.setOnRefreshListener {
            arrNews.clear()
            scroll.resetState()
            PreFragNews(this@NewsActivity).getNews(1)
        }

        recyNewsActi.addOnScrollListener(scroll)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
