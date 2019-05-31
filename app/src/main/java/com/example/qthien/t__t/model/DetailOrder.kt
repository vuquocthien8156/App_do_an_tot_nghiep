package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailOrder(
    @SerializedName("ma_chi_tiet")
    var idDetailOrder : Int,
    @SerializedName("ma_san_pham")
    var idProduct : Int,
    @SerializedName("ten")
    var nameProduct : String,
    @SerializedName("so_luong")
    var quantity : Int,
    @SerializedName("don_gia")
    var unitPrice : Long,
    @SerializedName("kich_co")
    var size : String,
    @SerializedName("gia_khuyen_mai")
    var priceDiscount : Long,
    @SerializedName("thanh_tien")
    var total : Long,
    @SerializedName("ghi_chu")
    var note : String,
    @SerializedName("topping")
    var arrTopping : ArrayList<ToppingOrder>?
    ) : Parcelable