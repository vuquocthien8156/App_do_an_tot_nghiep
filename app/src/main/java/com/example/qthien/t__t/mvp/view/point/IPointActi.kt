package com.example.qthien.t__t.mvp.view.point

import com.example.qthien.t__t.model.Point

interface IPointActi {
    fun successgetAllPointByUser(arrPoint : ArrayList<Point>)
    fun failure(message : String)
}