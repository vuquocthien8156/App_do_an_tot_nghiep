package com.example.qthien.t__t.mvp.view.main

import com.example.qthien.t__t.model.News

interface IViewFragNews {
    fun failureNews(message : String)
    fun successGetNews(arrNews : ArrayList<News>)
}
