package com.example.qthien.t__t.mvp.presenter.point

import com.example.qthien.t__t.model.Point

interface IPrePoint {
    fun successgetAllPointByUser(arrPoint : ArrayList<Point>)
    fun failure(message : String)
}