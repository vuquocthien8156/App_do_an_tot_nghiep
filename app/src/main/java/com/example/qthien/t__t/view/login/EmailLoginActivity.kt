package com.example.qthien.t__t.view.login

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.qthien.t__t.FragmentChosseGenderBottom
import com.example.qthien.t__t.R
import kotlinx.android.synthetic.main.activity_email_login.*
import kotlinx.android.synthetic.main.fragment_enter_info.*

class EmailLoginActivity : AppCompatActivity() ,  FragmentChosseGenderBottom.FragmentBottomGenderListener  {

    override fun resultChosseGender(b: Boolean) {
        val gender = when(b){
            true -> R.string.man
            else -> R.string.woman
        }
        val fragmentEnterInfo = supportFragmentManager.findFragmentByTag("enter_info") as EnterInfoFragment
        fragmentEnterInfo.setEdtGender(gender)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_login)
        setSupportActionBar(toolbarEmail)
        toolbarEmail.setTitleTextColor(Color.WHITE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        if(frmLoginEmail != null){
            if(savedInstanceState != null)
                return

            val enterEmailFragment = EnterEmailFragment()
            enterEmailFragment.arguments = intent.extras

            supportFragmentManager.beginTransaction().add(R.id.frmLoginEmail , enterEmailFragment).commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
