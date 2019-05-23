package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToppingProductCart(
    @SerializedName("ma_gio_hang")
    var idCart : Int,
    @SerializedName("ma_san_pham")
    var idProduct : Int,
    @SerializedName("ten")
    var nameProduct : String = "",
    @SerializedName("gia_san_pham")
    var priceProduct : Long = 0L,
    @SerializedName("so_luong")
    var quantity : Int = 0
) : Parcelable