package com.example.qthien.t__t.mvp.view.ealuation

import com.example.qthien.t__t.model.ChildEvaluation

interface IEvaluationDetail {
    fun successGetAllChildEvaluation(arrChild : ArrayList<ChildEvaluation>)
    fun succressAddChild(status : String?)
    fun failure(message : String)
}