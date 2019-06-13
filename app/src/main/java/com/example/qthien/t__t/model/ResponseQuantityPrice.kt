package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseQuantityPrice (
        @SerializedName("TotalQuantity")
        var totalQuantity : Int,
        @SerializedName("TotalCart")
        var totalPrice : Int
) : ResponseDefault()