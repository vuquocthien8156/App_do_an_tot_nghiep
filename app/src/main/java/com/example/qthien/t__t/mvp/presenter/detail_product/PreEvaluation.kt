package com.example.qthien.t__t.mvp.presenter.detail_product

import com.example.qthien.t__t.mvp.interactor.InEvaluation
import com.example.qthien.t__t.model.Evaluation
import com.example.qthien.t__t.mvp.view.ealuation.IEvaluation
import java.io.File

class PreEvaluation(var iEvaluation: IEvaluation) : IPreEvaluation {
    override fun successAddImg(arrUrl : ArrayList<String>) {
        iEvaluation.successAddImg(arrUrl)
    }

    override fun successAddEvaluation(status: String?) {
        iEvaluation.successAddEvaluation(status)
    }

    override fun failure(message: String) {
        iEvaluation.failure(message)
    }

    fun addEvaluate(evaluate: Evaluation) {
        InEvaluation(this).addEvaluate(evaluate)
    }

    fun addImagesEvaluate(arrFile : ArrayList<File>){
        InEvaluation(this).addImagesEvaluate(arrFile)
    }
}