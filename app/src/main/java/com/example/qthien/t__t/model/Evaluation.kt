package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

//ma_danh_gia :
//tai_khoan :
//san_pham :
//so_diem :
//tieu_de :
//noi_dung :
//thoi_gian :
//duyet :
//so_cam_on :
//images :
//listChild :

data class Evaluation(
        @SerializedName("ma_danh_gia")
        var idEvaluation: Int,
        @SerializedName("ten")
        var nameCustomer: String,
        @SerializedName("ma_san_pham")
        var idProduct: Int,
        @SerializedName("so_diem")
        var point: Int,
        @SerializedName("tieu_de")
        var title: String,
        @SerializedName("noi_dung")
        var content: String,
        @SerializedName("thoi_gian")
        var time: String,
        @SerializedName("duyet")
        var done: Int,
        @SerializedName("so_cam_on")
        var quantityTks: Int,
        @SerializedName("images")
        var images: ArrayList<String>,
        @SerializedName("listChild")
        var childs: ArrayList<ChildEvaluation>
)