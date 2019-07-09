package com.example.qthien.t__t.mvp.view.view_login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.mvp.presenter.pre_login.PreCheckExist
import com.example.qthien.t__t.mvp.view.customer.EnterEmailPhoneActivity
import com.facebook.accountkit.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import kotlinx.android.synthetic.main.fragment_enter_email.*

class EnterEmailFragment : Fragment() , ILogin , ICheckExistAccount{

    interface FragmentCallActivityLoginEmail{
        fun visibleLoader(b: Boolean)
        fun setBackPress(b: Boolean)
    }

    val APP_REQUEST_CODE = 99
    val REQUEST_FORGOT_PASS = 1
    var email = ""
    var emailConfirm = ""
    var fragmentCallActivityLoginEmail : FragmentCallActivityLoginEmail? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is FragmentCallActivityLoginEmail)
            fragmentCallActivityLoginEmail = context
    }

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
                PreCheckExist(this).checkExistAccount(email)
                txtShowError.setText("")
                Log.d("Testttt" , "1")
                fragmentCallActivityLoginEmail?.visibleLoader(true)
            }
            else
                txtShowError.setText(R.string.email_validation)
        })

        txtForgotPass.setOnClickListener {
            val i = Intent(context, EnterEmailPhoneActivity::class.java)
            i.putExtra("title", getString(com.example.qthien.t__t.R.string.forgot_pass))
            i.putExtra("txt_title", getString(com.example.qthien.t__t.R.string.enter_your_email))
            i.putExtra("hint", getString(com.example.qthien.t__t.R.string.your_email))
            i.putExtra("type" , "forgotPass")
            if(!edtEnterEmail.text.isNullOrEmpty())
                i.putExtra("text" , edtEnterEmail.text.toString())
            startActivityForResult(i, REQUEST_FORGOT_PASS)
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

    override fun resultExistAccount(email : String? , id_fb : String? , phone : String?) {
        Log.d("emaikkk" , email)
        if(!email.equals("")) {
            goToFragmentEnterPass(true)
        }
        else{
            if(edtEnterEmail.text.toString().equals(emailConfirm)){
                goToFragmentEnterPass(false)
            }
            else {
                fragmentCallActivityLoginEmail?.visibleLoader(false)
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setMessage(R.string.comfirm_mail)
                alertDialog.setPositiveButton(R.string.continute, { dialog, which ->
                    emailConfirm()
                })
                alertDialog.setNegativeButton(R.string.another_time, { dialog, which -> })
                alertDialog.show()
            }
        }
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
                }
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
        }

        if(REQUEST_FORGOT_PASS == requestCode && resultCode == Activity.RESULT_OK){
            val alert = android.support.v7.app.AlertDialog.Builder(context!!)
            alert.setCancelable(false)
            alert.setTitle(R.string.noti)
            alert.setMessage(R.string.noti_forgot_pass)
            alert.setPositiveButton(R.string.know , { dialog, which ->
                dialog.dismiss()
            })
            val dialog = alert.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
    }

    fun getEmailAccountkit(){
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(p0: Account?) {
                email = p0?.email.toString()
                emailConfirm = email
                if(!emailConfirm.equals(edtEnterEmail.text.toString())) {
                    Toast.makeText(context, R.string.email_confirm_error, Toast.LENGTH_LONG).show()
                    fragmentCallActivityLoginEmail?.visibleLoader(false)
                }else
                    goToFragmentEnterPass(false)
            }

            override fun onError(p0: AccountKitError?) {
                Log.d("ErrorAcountKit" , p0.toString())
                fragmentCallActivityLoginEmail?.visibleLoader(false)
            }
        })
    }

    private fun showErrorActivity(error: AccountKitError) {
        fragmentCallActivityLoginEmail?.visibleLoader(false)
    }

    override fun failure(message: String) {
    }

    override fun resultRegisterAccount(idUser: Int?) {
    }

    override fun resultLoginAccount(customer: Customer?) {
    }

    override fun resultLoginPhone(customer: Customer?) {
    }

    override fun resultLoginFacebook(customer: Customer?) {
    }

}