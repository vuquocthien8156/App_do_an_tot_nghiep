package com.example.qthien.t__t.presenter.branch

import com.example.qthien.t__t.interactor.InBranch
import com.example.qthien.t__t.model.BranchFolowArea
import com.example.qthien.t__t.view.branch.IBranchActivity

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