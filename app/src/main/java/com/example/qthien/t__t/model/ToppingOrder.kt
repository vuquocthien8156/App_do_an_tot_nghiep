package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToppingOrder (
    @SerializedName("ma_san_pham")
    var idProduct : Int,
    @SerializedName("ten")
    var nameProduct : String,
    @SerializedName("don_gia")
    var unitPrice : Long,
    @SerializedName("so_luong")
    var quantity : Int
) : Parcelable