package com.example.qthien.t__t.view.view_login

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
import com.example.qthien.t__t.presenter.pre_login.PreLogin
import kotlinx.android.synthetic.main.fragment_enter_password.*
import kotlinx.android.synthetic.main.fragment_enter_password.view.*

class EnterPassFragment : Fragment() , ILogin{

    var isAcountNew : Boolean? = false
    var email : String? = ""

    var callActivity : EnterInfoFragment.ResultFragmentCallActivity? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is EnterInfoFragment.ResultFragmentCallActivity)
            callActivity = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isAcountNew = arguments?.getBoolean("b")
        Log.d("isAcountNew" , isAcountNew.toString())
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
                setHideOrShowPass(HideReturnsTransformationMethod.getInstance() , R.drawable.ic_visibility)
            }
            else{
                setHideOrShowPass(PasswordTransformationMethod.getInstance() , R.drawable.ic_visibility_off)
            }
        })

        if(isAcountNew ?: true){
            txtTitlePass.setText(R.string.enter_pass_login)
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.enter_pass)
        } else{
            txtTitlePass.setText(R.string.enter_pass_register)
            (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.create_pass)
        }

        btnContinutePassword.setOnClickListener({
            if(edtEnterPassword.text.length > 2){
                evenClickBtnContinute()
                txtShowErrorPass.setText("")
            } else
                txtShowErrorPass.setText(R.string.password_validate)
        })

        edtEnterPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (edtEnterPassword.text.length > 6) {
                    txtShowErrorPass.setText("")
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun setHideOrShowPass(method: TransformationMethod, icon: Int) {
        edtEnterPassword.transformationMethod = method
        ibtnShowHidePass.setImageResource(icon)
        ibtnShowHidePass.setTag(icon)
        edtEnterPassword.setSelection(edtEnterPassword.text.length)
    }


    fun evenClickBtnContinute(){
        if(isAcountNew ?: true){
            Log.d("isAcountNew" , "true")
            PreLogin(this).login(email!! , edtEnterPassword.text.toString())
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

    override fun resultExistAccount(email : String? , id_fb : String? , phone : String?) {}

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
    }

    override fun resultRegisterAccount(idUser : Int?) {}

    override fun resultLoginAccount(customer: Customer?) {
        if(customer != null) {
            callActivity?.popAllBackStack(customer, 0)
            Log.d("isAcountNew" , customer.toString())
        }else
            txtShowErrorPass.setText(R.string.error_pass)
    }

    override fun resultLoginPhone(customer: Customer?) {}

    override fun resultLoginFacebook(customer: Customer?) {}

}