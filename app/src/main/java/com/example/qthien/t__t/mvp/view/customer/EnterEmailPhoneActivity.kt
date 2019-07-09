package com.example.qthien.t__t.mvp.view.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.mvp.presenter.pre_login.PreCheckExist
import com.example.qthien.t__t.mvp.presenter.pre_login.PreForgotPass
import com.example.qthien.t__t.mvp.view.view_login.ICheckExistAccount
import com.example.qthien.t__t.mvp.view.view_login.IForgotPass
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import kotlinx.android.synthetic.main.activity_enter_email_phone.*
import java.util.regex.Pattern


class EnterEmailPhoneActivity : AppCompatActivity() , ICheckExistAccount , IForgotPass {

    override fun failure(message: String) {
        progressLoader.visibility = View.GONE
        btnContinute.visibility = View.VISIBLE
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun sendComfirmEmail(status: String?) {
        if(status != null && status.equals("Success")) {
            setResult(Activity.RESULT_OK, Intent())
            finish()
        } else {
            progressLoader.visibility = View.GONE
            btnContinute.visibility = View.VISIBLE
            Toast.makeText(this, R.string.fail_again, Toast.LENGTH_LONG).show()
        }
    }

    override fun resultExistAccount(email: String?, id_fb: String?, phone: String?) {
        if(type.equals("phone")){
            if(phone != ""){
                txtShowError.setText(com.example.qthien.t__t.R.string.error_phone_exist)
                progressLoader.visibility = View.GONE
                btnContinute.visibility = View.VISIBLE
            }
            else{
                verifiPhone(edtEnterEmailPhone.getText().toString())
                txtShowError.setText("")
            }
        }
        else{
            if(email != "") {
                if(type.equals("email")) {
                    txtShowError.setText(com.example.qthien.t__t.R.string.error_email_exist)
                }
                PreForgotPass(this).sendEmailForgot(email!!)
            }else {
                if(type.equals("email")) {
                    txtShowError.setText("")
                    val i = Intent(this, CustomerActivity::class.java)
                    i.putExtra("email", edtEnterEmailPhone.text.toString())
                    setResult(Activity.RESULT_OK, i)
                    finish()
                }else{
                    progressLoader.visibility = View.GONE
                    btnContinute.visibility = View.VISIBLE
                    txtShowError.setText(com.example.qthien.t__t.R.string.erorr_now)
                }
            }
        }
    }

    var type : String? = ""
    val APP_REQUEST_CODE = 1
    var text : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_enter_email_phone)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(intent.extras?.getString("title")!!)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_close)

        txtTitleMailPhone.setText(intent.extras?.getString("txt_title")!!)
        edtEnterEmailPhone.setHint(intent.extras?.getString("hint")!!)

        type = intent.extras?.getString("type")
        text = intent.extras?.getString("text")

        if(text != null){
            edtEnterEmailPhone.setText(text)
            edtEnterEmailPhone.setSelection(text!!.length)
        }

        if(type.equals("phone"))
            edtEnterEmailPhone.inputType = InputType.TYPE_CLASS_PHONE
        else
            edtEnterEmailPhone.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        btnContinute.setOnClickListener({
            txtShowError.setText("")
            if(type.equals("phone")) {
                val phone = edtEnterEmailPhone.getText().toString()
                if (phone.isPhoneNumber()) {
                    progressLoader.visibility = View.VISIBLE
                    btnContinute.visibility = View.GONE
                    PreCheckExist(this).checkExistAccount(phone.replaceFirst("0" , "+84"))
                } else {
                    txtShowError.setText(com.example.qthien.t__t.R.string.error_phone)
                }
            }
            else{
                val email = edtEnterEmailPhone.getText().toString()
                if (isValidEmail(email)) {
                    progressLoader.visibility = View.VISIBLE
                    btnContinute.visibility = View.GONE
                    PreCheckExist(this).checkExistAccount(email)
                } else {
                    txtShowError.setText(com.example.qthien.t__t.R.string.error_email)
                }
            }
        })
    }

    fun String.isPhoneNumber() : Boolean {
        val check : Boolean
        if(Pattern.matches("^[+]?[0-9]{9,11}$", this))
            check = true
        else
            check = false

        return check
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) false else android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun verifiPhone(phone : String) {
        val intent = Intent(this , AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
            LoginType.PHONE,
            AccountKitActivity.ResponseType.TOKEN   // or .ResponseTyadditionalpe.CODE
        )
        configurationBuilder.setInitialPhoneNumber(PhoneNumber("+84" , phone.replaceFirst("0" , "") , "VNM"))
        intent.putExtra(
            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
            configurationBuilder.build()
        )
        startActivityForResult(intent, APP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result account kit
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            val loginResult = data?.getParcelableExtra<AccountKitLoginResult>(AccountKitLoginResult.RESULT_KEY)
            var toastMessage = ""
            if(loginResult != null){
                if (loginResult.error != null) {
                    toastMessage = loginResult.error!!.errorType.message
                    showErrorActivity(loginResult.error!!)
                    progressLoader.visibility = View.GONE
                    btnContinute.visibility = View.VISIBLE
                } else if (loginResult.wasCancelled()) {
                    progressLoader.visibility = View.GONE
                    btnContinute.visibility = View.VISIBLE
                } else {
                    if (loginResult.accessToken != null) {
                        toastMessage = "Success "
                    } else {
                        toastMessage = String.format(
                            "Successsss:%s...",
                            loginResult.authorizationCode!!.substring(0, 10)
                        )
                    }
                    // If you have an authorization code, retrieve it from
                    // loginResult.getAuthorizationCode()
                    // and pass it to your server and exchange it for an access token.
                    // Success! Start your next activity...
                    AccountKit.logOut()
                    successPhone()
                }
            }
        }
    }

    private fun successPhone() {
        val i = Intent(this , CustomerActivity::class.java)
        i.putExtra("phoneResult" , edtEnterEmailPhone.text.toString().replaceFirst("84" , "0"))
        setResult(Activity.RESULT_OK , i)
        finish()
    }

    fun getPhoneAccountkit(){
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(p0: Account?) {
                progressLoader.visibility = View.GONE
                btnContinute.visibility = View.VISIBLE
                val phone = p0?.phoneNumber.toString()
                if (phone.replaceFirst("+84", "0").equals(edtEnterEmailPhone.text.toString()))
                    successPhone()
                else {
                    Toast.makeText(this@EnterEmailPhoneActivity, R.string.phone_change_not_allow, Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onError(p0: AccountKitError?) {
                Log.d("ErrorAcountKit" , p0.toString())
                progressLoader.visibility = View.GONE
                btnContinute.visibility = View.VISIBLE
            }
        })
    }

    private fun showErrorActivity(error: AccountKitError) {

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
