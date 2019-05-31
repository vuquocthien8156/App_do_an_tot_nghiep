package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order (
    @SerializedName("ma_don_hang")
    var idOrder : Int,
    @SerializedName("thong_tin_giao_hang")
    var infoOrder : String,
    @SerializedName("ma_khach_hang")
    var idAccount : Int,
    @SerializedName("ngay_lap")
    var dateFounded : String?,
    @SerializedName("khuyen_mai")
    var discount : Int,
    @SerializedName("phi_ship")
    var priceTranfrom : Long,
    @SerializedName("tong_tien")
    var priceTotal : Long,
    @SerializedName("ghi_chu")
    var noteOrder : String,
    @SerializedName("trang_thai")
    var listStatus : ArrayList<StatusOrder>
) : Parcelable