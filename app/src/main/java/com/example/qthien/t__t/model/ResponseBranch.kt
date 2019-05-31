package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseBranch(
    @SerializedName("Branch")
    var arrBranch : ArrayList<BranchFolowArea>
) : ResponseDefault()