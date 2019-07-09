package com.example.qthien.t__t.mvp.view.ealuation

interface IEvaluation {
    fun successAddEvaluation(status : String?)
    fun successAddImg(arrUrl : ArrayList<String>)
    fun failure(message : String)
}