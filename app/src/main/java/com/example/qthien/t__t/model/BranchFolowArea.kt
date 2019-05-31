package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class BranchFolowArea(
    @SerializedName("ma_khu_vuc")
    var idArea : Int,
    @SerializedName("ten_khu_vuc")
    var nameArea : String,
    @SerializedName("Place")
    var arrBranch : ArrayList<Branch>
)