package com.example.qthien.t__t.view.view_login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.qthien.t__t.FragmentChosseGenderBottom
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Customer

class EnterInfoActivity : AppCompatActivity() , FragmentChosseGenderBottom.FragmentBottomGenderListener,EnterInfoFragment.FragmentCallActivityEnterInfo {

    lateinit var enterInfoFragment : EnterInfoFragment

    override fun resultChosseGender(b: Boolean) {
        var gender = 0
        if(b) gender = R.string.man else gender = R.string.woman
        enterInfoFragment.setEdtGender(gender)
    }

    override fun result(customer: Customer) {
        val intent = Intent(this , LoginActivity::class.java)
        intent.putExtra("customer" , customer)
        setResult(Activity.RESULT_OK , intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_info)

        enterInfoFragment = EnterInfoFragment()
        val bundle = Bundle()
        bundle.putString("phone" , intent?.extras?.getString("phone"))
        enterInfoFragment.arguments = bundle
//        Log.d("isAcountNeww" , isAcountNew.toString())
        supportFragmentManager.beginTransaction()
            .replace(R.id.frmInfo , enterInfoFragment, "enter_info").commit()
}
}
