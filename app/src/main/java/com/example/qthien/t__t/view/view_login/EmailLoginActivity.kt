package com.example.qthien.t__t.view.view_login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.qthien.t__t.FragmentChosseGenderBottom
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Customer
import kotlinx.android.synthetic.main.activity_email_login.*

class EmailLoginActivity : AppCompatActivity() , EnterInfoFragment.ResultFragmentCallActivity
    ,  FragmentChosseGenderBottom.FragmentBottomGenderListener  {

    override fun popAllBackStack(customer: Customer , size : Int) {
        for(i in 0..size){
            supportFragmentManager.popBackStack()
        }
        val intent = Intent(this , LoginActivity::class.java)
        Log.d("cccccccc" ,"3" + customer.toString())

        intent.putExtra("customer" , customer)
        setResult(Activity.RESULT_OK , intent)
        finish()
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

    override fun resultChosseGender(b: Boolean) {
        val gender = when(b){
            true -> R.string.man
            else -> R.string.woman
        }
        val fragmentEnterInfo = supportFragmentManager.findFragmentByTag("enter_info") as EnterInfoFragment
        fragmentEnterInfo.setEdtGender(gender)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
