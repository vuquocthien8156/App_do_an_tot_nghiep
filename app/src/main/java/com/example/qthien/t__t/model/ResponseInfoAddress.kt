package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseInfoAddress (
        @SerializedName("listAddress")
     var arrAddressInfo : ArrayList<InfoAddress>
) : ResponseDefault()