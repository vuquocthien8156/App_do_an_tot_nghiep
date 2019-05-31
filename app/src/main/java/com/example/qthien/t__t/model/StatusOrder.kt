package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StatusOrder (
    @SerializedName("ten_trang_thai")
    var nameStatus : String ,
    @SerializedName("thoi_gian")
    var timeStatus : String
) : Parcelable