package com.example.qthien.t__t.view.main

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.qthien.t__t.R
import kotlinx.android.synthetic.main.activity_enter_email_phone.*

class EnterEmailPhoneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_email_phone)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(intent.extras?.getString("title")!!)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        txtTitleMailPhone.setText(intent.extras?.getString("txt_title")!!)
        edtEnterEmailPhone.setHint(intent.extras?.getString("hint")!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
