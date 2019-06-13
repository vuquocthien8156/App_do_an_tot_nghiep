package com.example.qthien.t__t.view.ealuation

interface IEvaluation {
    fun successAddEvaluation(status : String?)
    fun successAddImg(arrUrl : ArrayList<String>)
    fun failure(message : String)
}