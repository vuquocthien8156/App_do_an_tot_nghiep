package com.example.qthien.t__t.mvp.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.mvp.presenter.pre_login.PreCheckExist
import com.example.qthien.t__t.mvp.presenter.pre_login.PreUpdatePhone
import com.example.qthien.t__t.mvp.view.view_login.ICheckExistAccount
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.update_phone_fragment.*

class UpdateNumberPhone : DialogFragment() , ICheckExistAccount , IUpdatePhone {
    override fun successUpdatePhone(status: String?) {
        if(status != null && status.equals("Success")) {
            Log.d("logggoutt" , "$phonee -- ")
            MainActivity.customer?.phoneNumber = phonee.replace("84" , "0")
            dismiss()
        }else{
            Toast.makeText(context , R.string.fail_again , Toast.LENGTH_LONG).show()
            MainActivity.customer?.phoneNumber = null
        }
    }

    override fun resultExistAccount(email: String?, id_fb: String?, phone: String?) {
        if(phone != ""){
            txtError.visibility = View.VISIBLE
            txtError.setText(R.string.error_phone_exist)
            progressLoader.visibility = View.GONE
            txtLogout.isEnabled = true
            MainActivity.customer?.phoneNumber = null
        }
        else{
            Log.d("phoneee" , phonee)
            PreUpdatePhone(this).updatePhone(MainActivity.customer!!.idCustomer , phonee)
        }
    }

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
    }

    val APP_REQUEST_CODE = 1
    var phonee = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.update_phone_fragment , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        txtLogout.setOnClickListener({
            logOut()
        })

        edtUpdatePhone.setOnClickListener({
            progressLoader.visibility = View.VISIBLE
            txtLogout.isEnabled = false
            eventVerifyPhone()
        })
    }

    fun eventVerifyPhone(){
        val intent = Intent(context , AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE,
                AccountKitActivity.ResponseType.TOKEN
        )
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build()
        )
        MainActivity.customer?.phoneNumber = "0123"
        startActivityForResult(intent, APP_REQUEST_CODE)
    }

    val logOut = fun(){

        if (AccountKit.getCurrentAccessToken() != null) {
            AccountKit.logOut()
        }

        if(com.facebook.AccessToken.getCurrentAccessToken() != null){
            LoginManager.getInstance().logOut()
        }
        context!!.getSharedPreferences("Login" , Context.MODE_PRIVATE).edit().clear().apply()
        MainActivity.customer = null

        when(parentFragment){
            is NewsFragment -> {
                dialog.dismiss()
                (parentFragment as NewsFragment).setLogout()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("logggoutt" , "onActivityResult")
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            val loginResult = data?.getParcelableExtra<AccountKitLoginResult>(AccountKitLoginResult.RESULT_KEY)
            var toastMessage = ""
            if(loginResult != null){
                if (loginResult.error != null) {
                    toastMessage = loginResult.error!!.errorType.message
                    MainActivity.customer?.phoneNumber = null
                    progressLoader.visibility = View.GONE
                    txtLogout.isEnabled = true
                    showErrorActivity(loginResult.error!!)
                } else if (loginResult.wasCancelled()) {
                    toastMessage = "Login Cancelled"
                    MainActivity.customer?.phoneNumber = null
                    progressLoader.visibility = View.GONE
                    txtLogout.isEnabled = true
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
                }
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
            getPhoneAccountkit()
        }

    }

    fun getPhoneAccountkit(){
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(p0: Account?) {
                phonee = p0?.phoneNumber.toString()
                Log.d("phoneee" , phonee)
                PreCheckExist(this@UpdateNumberPhone).checkExistAccount(phonee)
                phonee = phonee.replace("+" , "")
                AccountKit.logOut()
            }

            override fun onError(p0: AccountKitError?) {
                Log.d("ErrorAcountKit" , p0.toString())
            }
        })
    }


    private fun showErrorActivity(error: AccountKitError) {

    }
}