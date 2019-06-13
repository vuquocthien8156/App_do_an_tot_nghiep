package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ChildEvaluation (
        @SerializedName("ma_tk")
        var idAccount : Int = 0,
        @SerializedName("ma_danh_gia_con")
        var idChildEvaluation : Int,
        @SerializedName("ma_danh_gia")
        var idEvaluation : Int ,
        @SerializedName("ten")
        var nameCustomer : String,
        @SerializedName("noi_dung")
        var content : String,
        @SerializedName("thoi_gian")
        var time : String,
        @SerializedName("duyet")
        var done : Int
) : Parcelable