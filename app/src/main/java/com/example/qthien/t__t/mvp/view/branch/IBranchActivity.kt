package com.example.qthien.t__t.mvp.view.branch

import com.example.qthien.t__t.model.BranchFolowArea

interface IBranchActivity {
    fun getBranchFolow(arrBranch: ArrayList<BranchFolowArea>?)
    fun failureBranch(message : String)
}