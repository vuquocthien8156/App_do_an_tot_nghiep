package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Evaluation(
        @SerializedName("id_tk")
        var idAccount : Int = 0,
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
        @SerializedName("Hinh_anh")
        var images: ArrayList<String>,
        @SerializedName("danh_gia_con")
        var childs: ArrayList<ChildEvaluation>
) : Parcelable