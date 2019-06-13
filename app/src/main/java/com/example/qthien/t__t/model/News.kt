package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

//+ ma_tin_tuc : int (primary key)
//+ ten_tin_tuc : text
//+ noi_dung : text
//+ ngay_dang : text
//+ hinh_tin_tuc : text
//+ ngay_tao: text
//+ da_xoa : int(1)

data class News(
        @SerializedName("ma_tin_tuc")
        var idNews: Int,
        @SerializedName("ten_tin_tuc")
        var nameNews: String,
        @SerializedName("noi_dung")
        var contentNews: String,
        @SerializedName("ngay_dang")
        var date: String,
        @SerializedName("hinh_tin_tuc")
        var imageNews: String,
        @SerializedName("ngay_tao")
        var dateCreate: String
)