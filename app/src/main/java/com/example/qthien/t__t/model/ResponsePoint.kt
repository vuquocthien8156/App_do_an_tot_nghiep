package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponsePoint (
    @SerializedName("listPoint")
    var arrPoint : ArrayList<Point>
) : ResponseDefault()
