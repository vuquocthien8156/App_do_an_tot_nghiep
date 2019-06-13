package com.example.qthien.t__t.presenter.pre_frag_news

import com.example.qthien.t__t.model.News

interface IPreFragNews {
    fun failureNews(message : String)
    fun successGetNews(arrNews : ArrayList<News>)
}