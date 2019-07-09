package com.example.qthien.t__t.mvp.presenter.branch

import com.example.qthien.t__t.model.BranchFolowArea

interface IPreBranch {
    fun getBranchFolow(arrBranch: ArrayList<BranchFolowArea>?)
    fun failureBranch(message : String)
}