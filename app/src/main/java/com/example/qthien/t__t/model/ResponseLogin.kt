package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    @SerializedName("info")
    var customer : Customer?
)  : ResponseDefault()