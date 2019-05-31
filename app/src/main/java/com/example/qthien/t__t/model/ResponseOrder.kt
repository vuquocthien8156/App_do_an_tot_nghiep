package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseOrder (
    @SerializedName("Order")
    var arrOrder : ArrayList<Order>?
) : ResponseDefault()