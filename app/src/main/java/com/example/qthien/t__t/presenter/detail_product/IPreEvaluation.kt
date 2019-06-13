package com.example.qthien.t__t.presenter.detail_product

interface IPreEvaluation {
    fun successAddEvaluation(status : String?)
    fun successAddImg(arrUrl : ArrayList<String>)
    fun failure(message : String)
}