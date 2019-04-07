package com.example.qthien.t__t.view.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.view.MainActivity
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.FacebookCallback
import com.facebook.accountkit.*
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.toolbar_login.*
import java.util.*


class LoginActivity : AppCompatActivity() {

    var accessToken : AccessToken? = null
    val APP_REQUEST_CODE = 99

    var callbackManager : CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar)

        accessToken = AccountKit.getCurrentAccessToken()

        callbackManager = CallbackManager.Factory.create()

        if (accessToken != null) {
            //Handle Returning User
        } else {
            //Handle new or logged out user
        }
    }

    fun loginFacebook(view : View){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    ToastString(loginResult.accessToken.toString())
                    startActivity(Intent(this@LoginActivity , MainActivity::class.java))
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    // App code
                }
            })
    }

    fun loginEmail(view : View){
        startActivity(Intent(this , EmailLoginActivity::class.java))
    }

    fun phoneLogin(view: View) {
        val intent = Intent(this , AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
            LoginType.PHONE,
            AccountKitActivity.ResponseType.TOKEN
        ) // or .ResponseTyadditionalpe.CODE
//        configurationBuilder.setInitialPhoneNumber(PhoneNumber())
        intent.putExtra(
            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
            configurationBuilder.build()
        )
        startActivityForResult(intent, APP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        // Facebook
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        // Result account kit
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            val loginResult = data.getParcelableExtra<AccountKitLoginResult>(AccountKitLoginResult.RESULT_KEY)
            var toastMessage: String = ""
            if (loginResult.error != null) {
                toastMessage = loginResult.error!!.errorType.message
                showErrorActivity(loginResult.error!!)
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled"
            } else {
                if (loginResult.accessToken != null) {
                    toastMessage = "Success "
                    AccountKit.getCurrentAccount(object : AccountKitCallback<Account>{
                        override fun onSuccess(p0: Account?) {
                            ToastString(p0?.phoneNumber.toString())
                        }

                        override fun onError(p0: AccountKitError?) {
                            Log.d("ErrorAcountKit" , p0.toString())
                        }
                    })
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
                goToMyLoggedInActivity()
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun showErrorActivity(error: AccountKitError) {
        ToastString(error.toString())
    }

    private fun goToMyLoggedInActivity() {
        startActivity(Intent(this@LoginActivity , MainActivity::class.java))
    }

    fun ToastString(content : String){
        Toast.makeText(this , content , Toast.LENGTH_LONG).show()
    }
}
