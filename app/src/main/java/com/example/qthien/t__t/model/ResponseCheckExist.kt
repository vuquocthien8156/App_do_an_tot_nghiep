package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

class ResponseCheckExist(
    @SerializedName("email")
    var email : String?,
    @SerializedName("id_fb")
    var id_fb : String?,
    @SerializedName("phone")
    var phone : String?
) : ResponseDefault()