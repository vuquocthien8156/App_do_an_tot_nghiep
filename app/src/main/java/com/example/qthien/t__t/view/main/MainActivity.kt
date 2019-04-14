package com.example.qthien.t__t.view.main

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.qthien.t__t.R
import com.facebook.accountkit.AccountKit
import com.facebook.login.LoginManager


class MainActivity : AppCompatActivity() {

    @SuppressLint("PackageManagerGetSignatures")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
    }

    fun logOut(view : View){
        AccountKit.logOut()
        LoginManager.getInstance().logOut()
        finish()
    }
}
