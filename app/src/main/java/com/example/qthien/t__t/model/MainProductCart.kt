package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainProductCart (
    @SerializedName("id")
    var idProductCart : Int,
    @SerializedName("ma_san_pham")
    var idProduct : Int,
    @SerializedName("ten")
    var nameProduct : String = "",
    @SerializedName("gia_san_pham")
    var priceProduct : Long = 0L,
    @SerializedName("gia_vua")
    var priceMProduct : Long = 0L,
    @SerializedName("gia_lon")
    var priceLProduct : Long = 0L,
    @SerializedName("loai_chinh")
    var mainCatalogy : Int,
    @SerializedName("hinh_san_pham")
    var imageUrl : String = "",
    @SerializedName("so_luong")
    var quantity : Int = 0,
    @SerializedName("kich_co")
    var size : String = "",
    @SerializedName("ghi_chu")
    var note : String? = "",
    @SerializedName("topping")
    var arrTopping : ArrayList<ToppingProductCart>
) : Parcelable