package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class Branch (
    @SerializedName("ma_chi_nhanh")
    var idBranch : Int,
    @SerializedName("ten")
    var nameBranch : String,
    @SerializedName("dia_chi")
    var addressBranch : String,
    @SerializedName("latitude")
    var latitude : String,
    @SerializedName("longitude")
    var longitude : String,
    @SerializedName("ngay_khai_truong")
    var dateOpening : String,
    @SerializedName("gio_mo_cua")
    var timeOpen : String,
    @SerializedName("gio_dong_cua")
    var timeClose : String,
    @SerializedName("so_dien_thoai")
    var phoneNumberBranch : String
)