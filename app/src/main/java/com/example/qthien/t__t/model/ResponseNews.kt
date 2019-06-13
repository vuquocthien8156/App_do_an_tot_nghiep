package com.example.qthien.t__t.model

import com.google.gson.annotations.SerializedName

data class ResponseNews(
        @SerializedName("listNews")
        var arrNew: ArrayList<News>
) : ResponseDefault()