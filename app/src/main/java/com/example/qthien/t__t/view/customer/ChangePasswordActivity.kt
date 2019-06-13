package com.example.qthien.t__t.view.customer

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.presenter.pre_customer.PreChangePass
import com.example.qthien.t__t.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_change_password.*
import java.math.BigInteger
import java.security.MessageDigest


class ChangePasswordActivity : AppCompatActivity() , IChangePass{
    override fun failure(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
        MainActivity.customer?.password =  null
    }

    override fun successChangePass(status: String?) {
        if (status != null && status.equals("Success")) {
            Toast.makeText(this, R.string.change_pass_success, Toast.LENGTH_LONG).show()
            edtPassCurrent.setText("")
            edtPassNew.setText("")
            edtPassConfirm.setText("")
            finish()
        } else {
            Toast.makeText(this, R.string.fail_again, Toast.LENGTH_LONG).show()
            MainActivity.customer?.password =  null
        }
    }

    var passOld : String? = ""
    var email : String? = ""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.password)
        setContentView(com.example.qthien.t__t.R.layout.activity_change_password)

        setSupportActionBar(toolbarChangePass)
        toolbarChangePass.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        passOld = MainActivity.customer?.password
        email = MainActivity.customer?.email
        Toast.makeText(this , email + passOld , Toast.LENGTH_LONG).show()
        if(email != null){
            txtEmail.setText("${txtEmail.text} $email")
        }

        if(passOld?.length != 32)
            relaEdtPassCurrent.visibility = View.GONE
        else
            relaEdtPassCurrent.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_change_pass , menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    fun loadAnimEdt(idString :  Int, edt : EditText){
        Toast.makeText(this , idString , Toast.LENGTH_LONG).show()
        val shake = AnimationUtils.loadAnimation(this, com.example.qthien.t__t.R.anim.shake_edittext)
        edt.startAnimation(shake)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == com.example.qthien.t__t.R.id.menuSave){
            val passCurrent = edtPassCurrent.text.toString()
            val passNew = edtPassNew.text.toString()
            val passComfirm = edtPassConfirm.text.toString()

            if(passCurrent.length <= 6 && passOld != null){
                loadAnimEdt(R.string.password_validate , edtPassCurrent)
                return false
            }

            Log.d("passss" , (passCurrent.md5().equals(passOld)).toString())
            if(!passCurrent.md5().equals(passOld) && passOld != null){
                loadAnimEdt(R.string.error_pass_old , edtPassCurrent)
                return false
            }

            if(passNew.length > 6)
                if(passNew.equals(passComfirm)){
                    MainActivity.customer?.password =  passNew.md5()
                    PreChangePass(this@ChangePasswordActivity).changePassword(MainActivity.customer!!.idCustomer , passNew.md5())
                } else
                    loadAnimEdt(R.string.erorr_pass_confirm , edtPassConfirm)
            else
                loadAnimEdt(R.string.password_validate , edtPassNew)
        }
        return super.onOptionsItemSelected(item)
    }
}
