package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class  InfoAddress (
    @SerializedName("id")
    var idAddress : Int,
    @SerializedName("ten_nguoi_nhan")
    var nameCustomer : String,
    @SerializedName("so_dien_thoai")
    var phoneCustomer : String,
    @SerializedName("dia_chi")
    var addressInfo : String,
    @SerializedName("chinh")
    var main : Int,
    var ma_tai_khoan : Int,
    var da_xoa : Int = 0
) : Parcelable