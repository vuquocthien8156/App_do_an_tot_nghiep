package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseDetailOrder (
    @SerializedName("Detail")
    val arrDetail : ArrayList<DetailOrder>
) : ResponseDefault()