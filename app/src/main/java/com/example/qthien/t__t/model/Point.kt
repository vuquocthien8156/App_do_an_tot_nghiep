package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class Point (
    @SerializedName("ma_don_hang")
    var idOrder : Int,
    @SerializedName("ma_chu")
    var idOrderText : String,
    @SerializedName("so_diem")
    var point : Int,
    @SerializedName("thoi_gian")
    var time : String,
    @SerializedName("hinh_thuc")
    var type : Int
)