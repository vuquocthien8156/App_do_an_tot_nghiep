package com.example.qthien.t__t.view.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.presenter.login.PreLogin
import com.facebook.accountkit.*
import kotlinx.android.synthetic.main.fragment_enter_email.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.LoginType
import com.facebook.accountkit.ui.AccountKitConfiguration



class EnterEmailFragment : Fragment() , ILogin{

    val APP_REQUEST_CODE = 99
    var email = ""

    var b = false

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.login_email)
        Log.d("aaaaaaaaaaaa", "b")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_enter_email, container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnContinuteEmail.setOnClickListener({
            email = edtEnterEmail.text.toString()
            if(isValidEmail(email)) {
                PreLogin(this).checkExistAccount(email)
                txtShowError.setText("")
            }
            else
                txtShowError.setText(R.string.email_validation)
        })

        txtForgotPass.setOnClickListener {
            test()
        }

        edtEnterEmail.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if (isValidEmail(edtEnterEmail.text.toString())) {
                    txtShowError.setText("")
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) false else android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()

    }

    override fun resultExistAccount(boolean: Boolean) {
        if(b) {
            goToFragmentEnterPass(b)
        }
        else{
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage(R.string.comfirm_mail)
            alertDialog.setPositiveButton(R.string.continute , DialogInterface.OnClickListener { dialog, which ->
                emailConfirm()
            })

            alertDialog.setNegativeButton(R.string.another_time , DialogInterface.OnClickListener { dialog, which -> })
            alertDialog.show()
        }
    }

    fun test(){
        b = !b
    }

    fun emailConfirm() {
        val intent = Intent(activity, AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
            LoginType.EMAIL,
            AccountKitActivity.ResponseType.TOKEN // or .ResponseType.CODE
        )
        // ... perform additional configuration ...
        configurationBuilder.setInitialEmail(edtEnterEmail.text.toString())
        intent.putExtra(
            AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
            configurationBuilder.build()
        )
        startActivityForResult(intent, APP_REQUEST_CODE)
    }

    fun goToFragmentEnterPass(boolean: Boolean){
        val enterPassFragment = EnterPassFragment()
        val bundle = Bundle()
        bundle.putBoolean("b" , boolean)
        bundle.putString("email" , email)
        enterPassFragment.arguments = bundle
        activity?.supportFragmentManager
            ?.beginTransaction()?.replace(R.id.frmLoginEmail , enterPassFragment)?.addToBackStack(null)?.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result account kit email
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

                    getEmailAccountkit()

                    AccountKit.logOut()

                    // Success! Start your next activity...
                    goToFragmentEnterPass(false)
                }
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
        }
    }

    fun getEmailAccountkit(){
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(p0: Account?) {
                email = p0?.email.toString()
            }

            override fun onError(p0: AccountKitError?) {
                Log.d("ErrorAcountKit" , p0.toString())
            }
        })
    }

    private fun showErrorActivity(error: AccountKitError) {

    }

}