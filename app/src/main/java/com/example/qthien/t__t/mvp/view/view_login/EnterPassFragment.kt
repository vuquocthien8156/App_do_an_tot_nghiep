package com.example.qthien.t__t.mvp.view.view_login

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.mvp.presenter.pre_login.PreLogin
import kotlinx.android.synthetic.main.fragment_enter_password.*
import kotlinx.android.synthetic.main.fragment_enter_password.view.*

class EnterPassFragment : Fragment() , ILogin{

    var isAcountNew : Boolean? = false
    var email : String? = ""
    var fragmentCallActivityLoginEmail : EnterEmailFragment.FragmentCallActivityLoginEmail? = null
    var callActivity : EnterInfoFragment.ResultFragmentCallActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is EnterInfoFragment.ResultFragmentCallActivity)
            callActivity = context
        if(context is EnterEmailFragment.FragmentCallActivityLoginEmail)
            fragmentCallActivityLoginEmail = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentCallActivityLoginEmail?.visibleLoader(false)

        isAcountNew = arguments?.getBoolean("b")
        email = arguments?.getString("email")
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_enter_password , container , false)
        view.ibtnShowHidePass.setImageResource(R.drawable.ic_visibility_off)
        view.ibtnShowHidePass.setTag(R.drawable.ic_visibility_off)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.ibtnShowHidePass.setOnClickListener({
            val re =  view.ibtnShowHidePass.getTag()
            if(re == R.drawable.ic_visibility_off){
                setHideOrShowPass(HideReturnsTransformationMethod.getInstance() , R.drawable.ic_visibility , it)
            }
            else{
                setHideOrShowPass(PasswordTransformationMethod.getInstance() , R.drawable.ic_visibility_off , it)
            }
        })

        view.ibtnShowHidePassConfirm.setOnClickListener({
            val re =  view.ibtnShowHidePassConfirm.getTag()
            if(re == R.drawable.ic_visibility_off){
                setHideOrShowPass(HideReturnsTransformationMethod.getInstance() , R.drawable.ic_visibility , it)
            }
            else{
                setHideOrShowPass(PasswordTransformationMethod.getInstance() , R.drawable.ic_visibility_off , it)
            }
        })

        if(isAcountNew ?: true){
            txtTitlePass.setText(R.string.enter_pass_login)
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.enter_pass)
            lnConfirmPass.visibility = View.GONE
        } else{
            txtTitlePass.setText(R.string.enter_pass_register)
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.create_pass)
            lnConfirmPass.visibility = View.VISIBLE
        }

        btnContinutePassword.setOnClickListener({
            if(edtEnterPassword.text.length > 6){
                var b = edtEnterPassword.text.toString().equals(edtEnterPasswordConfirm.text.toString())
                if(lnConfirmPass.visibility == View.GONE) b = true
                if(b){
                    evenClickBtnContinute()
                    txtShowErrorPass.setText("")
                    txtShowErrorPassConfirm.setText("")
                }
                else{
                    txtShowErrorPassConfirm.setText(R.string.erorr_pass_confirm)
                }
            } else
                txtShowErrorPass.setText(R.string.password_validate)
        })

        val textChange = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (edtEnterPassword.text.length > 6) {
                    txtShowErrorPass.setText("")
                }

                if(edtEnterPassword.text.equals(edtEnterPasswordConfirm.text)){
                    txtShowErrorPassConfirm.setText("")
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        edtEnterPassword.addTextChangedListener(textChange)
        edtEnterPasswordConfirm.addTextChangedListener(textChange)
    }

    private fun setHideOrShowPass(method: TransformationMethod, icon: Int, it: View) {
        when(it.id){
            R.id.ibtnShowHidePass ->{
                edtEnterPassword.transformationMethod = method
                ibtnShowHidePass.setImageResource(icon)
                ibtnShowHidePass.setTag(icon)
                edtEnterPassword.setSelection(edtEnterPassword.text.length)
            }
            R.id.ibtnShowHidePassConfirm ->{
                edtEnterPasswordConfirm.transformationMethod = method
                ibtnShowHidePassConfirm.setImageResource(icon)
                ibtnShowHidePassConfirm.setTag(icon)
                edtEnterPasswordConfirm.setSelection(edtEnterPasswordConfirm.text.length)
            }
        }
    }

    fun evenClickBtnContinute(){
        if(isAcountNew ?: true){
            Log.d("isAcountNew" , "true")
            fragmentCallActivityLoginEmail?.setBackPress(false)
            PreLogin(this).login(email!! , edtEnterPassword.text.toString())
            fragmentCallActivityLoginEmail?.visibleLoader(true)
        }else{
            val enterInfoFragment = EnterInfoFragment()
            val bundle = Bundle()
            bundle.putStringArrayList("email_pass" , arrayListOf(email , edtEnterPassword.text.toString()))
            enterInfoFragment.arguments = bundle
            Log.d("isAcountNeww" , isAcountNew.toString())
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.frmLoginEmail , enterInfoFragment, "enter_info").addToBackStack(null).commit()
        }
    }

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
        fragmentCallActivityLoginEmail?.visibleLoader(false)
        fragmentCallActivityLoginEmail?.setBackPress(true)
    }

    override fun resultRegisterAccount(idUser : Int?) {}

    override fun resultLoginAccount(customer: Customer?) {
        if (customer != null) {
            callActivity?.popAllBackStack(customer, 0)
        } else {
            txtShowErrorPass.setText(R.string.error_pass)
            fragmentCallActivityLoginEmail?.visibleLoader(false)
            fragmentCallActivityLoginEmail?.setBackPress(true)
        }
    }

    override fun resultLoginPhone(customer: Customer?) {}

    override fun resultLoginFacebook(customer: Customer?) {}

}