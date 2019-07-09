package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Discount (
        @SerializedName("ma_khuyen_mai")
        var idDiscount : Int,
        @SerializedName("ma_code")
        var idCode : String,
        @SerializedName("gioi_han_so_code")
        var limitCode : Int,
        @SerializedName("ma_con_lai")
        var codeLeft : Int,
        @SerializedName("hinh_anh")
        var image : String,
        @SerializedName("ten_khuyen_mai")
        var nameDiscount : String,
        @SerializedName("mo_ta")
        var decripDiscount : String?,
        @SerializedName("so_phan_tram")
        var persent : Int,
        @SerializedName("so_tien")
        var price : Int,
        @SerializedName("so_tien_qui_dinh_toi_thieu")
        var priceMinimun : Int,
        @SerializedName("ngay_bat_dau")
        var dateStart : String,
        @SerializedName("ngay_ket_thuc")
        var dateEnd : String,
        @SerializedName("ma_san_pham")
        var idProduct : Int,
        @SerializedName("hien_slider")
        var showSlider : Int,
        @SerializedName("da_xoa")
        var status : String
) : Parcelable