package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
    @SerializedName("ma_so")
    var idProduct : Int,
    @SerializedName("ten")
    var nameProduct : String,
    @SerializedName("gia_san_pham")
    var priceProduct : Long,
    @SerializedName("gia_vua")
    var priceMProduct : Long,
    @SerializedName("gia_lon")
    var priceLProduct : Long,
    @SerializedName("ngay_ra_mat")
    var launchDateProduct : String?,
    @SerializedName("hinh_san_pham")
    var imageProduct : String,
    @SerializedName("mo_ta")
    var decriptionProduct : String,
    @SerializedName("daxoa")
    var delete : Int,
    @SerializedName("loai_chinh")
    var mainCatalogy : Int
) : Parcelable