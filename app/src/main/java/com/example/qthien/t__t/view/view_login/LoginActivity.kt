package com.example.qthien.t__t.view.view_login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.presenter.pre_login.PreLogin
import com.example.qthien.t__t.view.main.MainActivity
import com.facebook.*
import com.facebook.accountkit.*
import com.facebook.accountkit.AccessToken
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : AppCompatActivity() , ILogin  {


    var accessTokenKit : AccessToken? = null
    var accessTokenFB : com.facebook.AccessToken? = null
    val APP_REQUEST_CODE = 99

    var callbackManager : CallbackManager? = null

    lateinit var preLogin : PreLogin
    val REQUEST_CODE_LOGIN_EMAIL = 1

    var phone = ""
    val REQUEST_CODE_INFO = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.choose_login)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        accessTokenKit = AccountKit.getCurrentAccessToken()
        accessTokenFB = com.facebook.AccessToken.getCurrentAccessToken()

        callbackManager = CallbackManager.Factory.create()

        preLogin = PreLogin(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun loginFacebook(view : View){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"))

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    lnLoader.visibility = View.GONE
                    getInfoUserFacebook()
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(exception: FacebookException) {
                    ToastString(exception.toString())
                }
            })
    }

    private fun getInfoUserFacebook(){

//        com.facebook.AccessToken(
//            "EAAefkhtJGoQBAFnV2anIPw0LMQYJAsQ0YEh4PlH1GjainawroJ0IlJrORXqke0N2CcIa5z5cHzhVFKbDokmePtqAoAx2iCtJbpRCszYws9gaiEah062jsT43zTkZBtZAGRrZA7Ev9jKknCfYoyNoeZC7AADb6QgAeNmZBZAlf9VNW2pP1ztDkx76WZASKHyZA1DTCWkzACjSpJRvXw3LZA1QdxzZBLy1jTwYEZD",
//            "2145774708791940",
//            "1759083097570150",
//            arrayListOf("email"), null , null,null,null,null
//        )
        GraphRequest(
            com.facebook.AccessToken.getCurrentAccessToken(),
            "/me?fields=id,name,email,picture",
            null,
            HttpMethod.GET, object :
                GraphRequest.Callback {
                override fun onCompleted(response: GraphResponse) {
                    if(response.jsonObject != null){
                        Log.d("Ahiiiiiiiiiii" , response.jsonObject.toString())

                        val id = response.jsonObject.get("id")
                        val name = response.jsonObject.get("name")
                        val email = response.jsonObject.get("email")
                        val url = response.jsonObject.getJSONObject("picture")
                            .getJSONObject("data").get("url")

                        MainActivity.customerFB = Customer(
                            0,
                            id.toString(),
                            name as String,
                            null,
                            null,
                            null,
                            null,
                            email as String,
                            null,
                            url as String,
                            null
                        )
                        preLogin.loginFacebook(id.toString() , email , name)
                    }
                    else {
                        ToastString(getString(R.string.fail_again))
                    }
                }
            }
        ).executeAsync()
    }


    fun loginEmail(view : View){
        startActivityForResult(Intent(this , EmailLoginActivity::class.java) , REQUEST_CODE_LOGIN_EMAIL)
    }

    fun phoneLogin(view: View) {
        val intent = Intent(this , AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
            LoginType.PHONE,
            AccountKitActivity.ResponseType.TOKEN   // or .ResponseTyadditionalpe.CODE
        )
//        configurationBuilder.setInitialPhoneNumber(PhoneNumber("" , "0782328156" , ""))
        intent.putExtra(
            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
            configurationBuilder.build()
        )
        startActivityForResult(intent, APP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
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
                    lnLoader.visibility = View.VISIBLE
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

                    getPhoneAccountkit()
                }
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
        }

        if(requestCode == REQUEST_CODE_LOGIN_EMAIL && resultCode == Activity.RESULT_OK && data != null){
            val customer = data.getParcelableExtra("customer") as Customer?
            val edit = getSharedPreferences("Login" , Context.MODE_PRIVATE).edit()
            edit.putString("email" , customer?.email).apply()
            Log.d("cccccccc" ,"1" + customer.toString())
            startIntent(customer)
        }

        if(requestCode == REQUEST_CODE_INFO && resultCode == Activity.RESULT_OK && data != null ){
            val customer = data.getParcelableExtra("customer") as Customer?
            val edit = getSharedPreferences("Login" , Context.MODE_PRIVATE).edit()
            edit.putString("phone" , customer?.phoneNumber).apply()
            Log.d("cccccccc" ,"1" + customer.toString())
            startIntent(customer)
        }
    }

    private fun showErrorActivity(error: AccountKitError) {
        ToastString(error.toString())
    }

    fun ToastString(content : String){
        Toast.makeText(this , content , Toast.LENGTH_LONG).show()
    }

    fun getPhoneAccountkit(){
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account>{
            override fun onSuccess(p0: Account?) {
                phone = p0?.phoneNumber.toString()
                ToastString(phone)
                preLogin.checkExistAccount(phone)
            }

            override fun onError(p0: AccountKitError?) {
                Log.d("ErrorAcountKit" , p0.toString())
                ToastString(getString(R.string.get_info_fail))
            }
        })
    }

    override fun failure(message: String) {
        ToastString(message)
    }

    override fun resultLoginPhone(customer: Customer?) {
        Log.d("resultLoginPhone" , "Doooo")
        lnLoader.visibility = View.GONE

        if(customer != null) {
            getSharedPreferences("Login" , Context.MODE_PRIVATE).edit()
                .putString("phone" , customer.phoneNumber).apply()
            startIntent(customer)
        }
        else
            ToastString(getString(R.string.fail_again))
    }

    override fun resultLoginFacebook(customer: Customer?){
        Log.d("resultLoginFacebook" , "Doooo")
        lnLoader.visibility = View.GONE
        if(customer != null)
            startIntent(customer)
        else
            ToastString(getString(R.string.fail_again))
    }

    fun startIntent(customer: Customer? , customerFB : Customer? = null){
        val intent = Intent(this , MainActivity::class.java)
        intent.putExtra("customer" , customer)
        setResult(Activity.RESULT_OK , intent)
        finish()
    }

    override fun resultExistAccount(email : String? , id_fb : String? , phone : String?) {
        if(!this.phone.equals("")) { // Login phone
            if (id_fb.equals("") && email.equals("")) {
                if(phone.equals("")) {
                    val i = Intent(this, EnterInfoActivity::class.java)
                    i.putExtra("phone", this.phone)
                    startActivityForResult(i, REQUEST_CODE_INFO)
                }
                else
                    preLogin.loginPhoneUser(phone!!)
            }
            else{
                lnLoader.visibility = View.GONE
                Toast.makeText(this , R.string.phone_account_exist, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun resultRegisterAccount(idUser : Int?) {}

    override fun resultLoginAccount(customer: Customer?) {}
}
