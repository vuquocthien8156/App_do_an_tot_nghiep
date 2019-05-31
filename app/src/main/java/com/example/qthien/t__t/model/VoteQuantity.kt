package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class VoteQuantity(
        @SerializedName("tong")
        var total: Int,
        @SerializedName("nam_diem")
        var totalFive: Int,
        @SerializedName("bon_diem")
        var totalFour: Int,
        @SerializedName("ba_diem")
        var totalThree: Int,
        @SerializedName("hai_diem")
        var totalTwo: Int,
        @SerializedName("mot_diem")
        var totalOne: Int
)