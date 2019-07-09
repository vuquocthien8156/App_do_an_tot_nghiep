package com.example.qthien.t__t.mvp.presenter.detail_product

import com.example.qthien.t__t.model.ChildEvaluation

interface IPreDetailEvaluation {
    fun successGetAllChildEvaluation(arrChild : ArrayList<ChildEvaluation>)
    fun succressAddChild(status : String?)
    fun failure(message : String)
}