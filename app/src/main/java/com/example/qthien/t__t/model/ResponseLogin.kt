package com.example.qthien.t__t.model

data class ResponseLogin(
    var status : String,
    var success : Boolean,
    var item : ArrayList<Account>
)