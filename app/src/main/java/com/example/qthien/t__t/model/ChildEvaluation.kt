package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

class ChildEvaluation (
        @SerializedName("ma_danh_gia_con")
        var idChildEvaluation : Int,
        @SerializedName("ma_danh_gia")
        var idEvaluation : Int ,
        @SerializedName("ten")
        var nameCustomer : String,
        @SerializedName("noi_dung")
        var content : String,
        @SerializedName("duyet")
        var done : Int
)