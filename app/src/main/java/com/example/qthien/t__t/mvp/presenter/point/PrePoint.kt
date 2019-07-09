package com.example.qthien.t__t.mvp.presenter.point

import com.example.qthien.t__t.model.Point
import com.example.qthien.t__t.mvp.interactor.InPoint
import com.example.qthien.t__t.mvp.view.point.IPointActi

class PrePoint(var iPoint : IPointActi) : IPrePoint {
    override fun successgetAllPointByUser(arrPoint: ArrayList<Point>) {
        iPoint.successgetAllPointByUser(arrPoint)
    }

    override fun failure(message: String) {
        iPoint.failure(message)
    }

    fun getAllPointByUser(idUser : Int) = InPoint(this).getAllPointByUser(idUser)
}