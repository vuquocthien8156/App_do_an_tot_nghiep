package com.example.qthien.t__t.mvp.presenter.branch

import com.example.qthien.t__t.mvp.interactor.InBranch
import com.example.qthien.t__t.model.BranchFolowArea
import com.example.qthien.t__t.mvp.view.branch.IBranchActivity

class PreBranch(var iBranchActivity: IBranchActivity) : IPreBranch {

    fun getBranchFolowArea(){
        InBranch(this).getBranchFolowArea()
    }

    override fun getBranchFolow(arrBranch: ArrayList<BranchFolowArea>?) {
        iBranchActivity.getBranchFolow(arrBranch)
    }

    override fun failureBranch(message: String) {
        iBranchActivity.failureBranch(message)
    }
}