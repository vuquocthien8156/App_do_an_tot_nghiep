package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CatalogyProduct(
    @SerializedName("ma_loai_sp")
    var idCatalogy : Int,
    @SerializedName("ten_loai_sp")
    var nameCatelogy : String?,
    @SerializedName("loai_chinh")
    var mainCatelogy : Int?

) : Parcelable