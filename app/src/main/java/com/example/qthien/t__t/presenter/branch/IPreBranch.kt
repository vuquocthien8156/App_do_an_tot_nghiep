package com.example.qthien.t__t.presenter.branch

import com.example.qthien.t__t.model.BranchFolowArea

interface IPreBranch {
    fun getBranchFolow(arrBranch: ArrayList<BranchFolowArea>?)
    fun failureBranch(message : String)
}