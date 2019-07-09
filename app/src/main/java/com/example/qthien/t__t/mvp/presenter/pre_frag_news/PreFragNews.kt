package com.example.qthien.t__t.mvp.presenter.pre_frag_news

import com.example.qthien.t__t.mvp.interactor.InFragNews
import com.example.qthien.t__t.model.News
import com.example.qthien.t__t.mvp.view.main.IViewFragNews

class PreFragNews(var iNews : IViewFragNews) : IPreFragNews {
    override fun failureNews(message: String) {
        iNews.failureNews(message)
    }

    override fun successGetNews(arrNews: ArrayList<News>) {
        iNews.successGetNews(arrNews)
    }

    fun getNews(page : Int){
        InFragNews(this).getNews(page)
    }
}