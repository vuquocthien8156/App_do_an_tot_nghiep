package com.example.qthien.t__t.view.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.Toast
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.presenter.pre_login.PreLogin
import com.example.qthien.t__t.view.view_login.ILogin
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitError
import com.facebook.accountkit.AccountKitLoginResult
import com.facebook.accountkit.PhoneNumber
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import kotlinx.android.synthetic.main.activity_enter_email_phone.*


class EnterEmailPhoneActivity : AppCompatActivity() , ILogin {

    override fun failure(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun resultExistAccount(email: String?, id_fb: String?, phone: String?) {
        if(phone != "")
            txtShowError.setText(com.example.qthien.t__t.R.string.error_phone_exist)
        else
            verifiPhone(edtEnterEmailPhone.getText().toString())
    }

    var type = ""
    val APP_REQUEST_CODE = 1
    val phoneRequest = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_enter_email_phone)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(intent.extras?.getString("title")!!)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_close)

        txtTitleMailPhone.setText(intent.extras?.getString("txt_title")!!)
        edtEnterEmailPhone.setHint(intent.extras?.getString("hint")!!)

        type = intent.extras?.getString("type")!!

        if(type.equals("phone"))
            edtEnterEmailPhone.inputType = InputType.TYPE_CLASS_PHONE

        btnContinute.setOnClickListener({
            if(type.equals("phone")) {
                val phone = edtEnterEmailPhone.getText().toString()
                if (checkValidatePhone(phone)) {
                    PreLogin(this).checkExistAccount(phone.replaceFirst("0" , "+84"))
                } else {
                    txtShowError.setText(com.example.qthien.t__t.R.string.error_phone)
                }
            }
        })
    }

    private fun checkValidatePhone(phone : String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches();
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
                } else if (loginResult.wasCancelled()) {
                    toastMessage = "Login Cancelled"
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

            // Surface the result to your user in an appropriate way.
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun successPhone() {
        val i = Intent(this , CustomerActivity::class.java)
        i.putExtra("phoneResult" , edtEnterEmailPhone.text.toString().replaceFirst("0" , "84"))
        setResult(Activity.RESULT_OK , i)
        finish()
    }

    private fun showErrorActivity(error: AccountKitError) {

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun resultRegisterAccount(idUser: Int?) {}

    override fun resultLoginAccount(customer: Customer?) {}

    override fun resultLoginPhone(customer: Customer?) {}

    override fun resultLoginFacebook(customer: Customer?) {}
}
