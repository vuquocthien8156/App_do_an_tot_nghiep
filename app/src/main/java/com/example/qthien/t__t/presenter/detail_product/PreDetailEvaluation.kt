package com.example.qthien.t__t.presenter.detail_product

import com.example.qthien.t__t.interactor.InEvaluationChild
import com.example.qthien.t__t.model.ChildEvaluation
import com.example.qthien.t__t.view.ealuation.IEvaluationDetail

class PreDetailEvaluation(var iEvaluationDetail: IEvaluationDetail) : IPreDetailEvaluation {
    override fun succressAddChild(status: String?) {
        iEvaluationDetail.succressAddChild(status)
    }

    override fun successGetAllChildEvaluation(arrChild: ArrayList<ChildEvaluation>) {
        iEvaluationDetail.successGetAllChildEvaluation(arrChild)
    }

    override fun failure(message: String) {
        iEvaluationDetail.failure(message)
    }

    fun getChildEvalute(idEvaluate: Int, page: Int) {
        InEvaluationChild(this).getChildEvalute(idEvaluate, page)
    }

    fun addChildEvaluate(childValuation: ChildEvaluation){
        InEvaluationChild(this).addReplyForEvaluation(childValuation)
    }
}