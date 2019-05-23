package com.example.qthien.t__t.view.view_login

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.example.qthien.t__t.FragmentChosseGenderBottom
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.presenter.pre_login.PreLogin
import kotlinx.android.synthetic.main.fragment_enter_info.*
import kotlinx.android.synthetic.main.fragment_enter_info.view.*
import java.text.SimpleDateFormat
import java.util.*

class EnterInfoFragment : Fragment() , ILogin {

    interface ResultFragmentCallActivity{
        fun popAllBackStack(customer: Customer , size : Int)
    }

    interface FragmentCallActivityEnterInfo{
        fun result(customer: Customer)
    }

    var customer : Customer? = null

    var callActivity : ResultFragmentCallActivity? = null
    var callActivityPhone : FragmentCallActivityEnterInfo? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is ResultFragmentCallActivity)
            callActivity = context

        if(context is FragmentCallActivityEnterInfo)
            callActivityPhone = context
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.enter_info)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val arrEmailPass = arguments?.getStringArrayList("email_pass")
        customer = Customer(0, "", "", "", 0, 0, "", "", "", "", "")

        if(arrEmailPass?.size != null) {
            customer?.email = arrEmailPass[0]
            customer?.password = arrEmailPass[1]
        }
        else{
            customer?.email = ""
            customer?.password = ""
        }

        val phone = arguments?.getString("phone")
        if(phone != null)
            customer?.phoneNumber = ""


        return layoutInflater.inflate(R.layout.fragment_enter_info , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.edtGender.setOnClickListener {
            showDialogChosseGender()
        }

        view.edtBirthday.setOnClickListener({
            selectDate()
        })

        view.btnContinuteInfo.setOnClickListener({
            if(checkValidate()){
                val arrBirthday = edtBirthday.text.toString().split("/")
                customer?.birthday = "${arrBirthday[2]}/${arrBirthday[1]}/${arrBirthday[0]}"
                customer?.nameCustomer = edtFullName.text.toString()
                customer?.gender = when(edtGender.text.toString()){
                    "Nam" -> 0
                    else -> 1
                }
                PreLogin(this).register(customer!!)
            }
        })

    }

    fun showDialogChosseGender(){
        val fragmentChosseGenderBottom = FragmentChosseGenderBottom()
        fragmentChosseGenderBottom.show(activity?.supportFragmentManager , "dialog")
    }

    fun setEdtGender(gender: Int) {
        edtGender.setText(gender)
    }

    @SuppressLint("SimpleDateFormat")
    fun selectDate(){
        var year = 0
        var month = 0
        var day = 0
        val calender = Calendar.getInstance()

        if(edtBirthday.getText()?.length != 0) {
            val simpleDateFormat = SimpleDateFormat("d/M/y")
            calender.time = simpleDateFormat.parse(edtBirthday.getText().toString())
        }

        year = calender.get(Calendar.YEAR)
        month = calender.get(Calendar.MONTH)
        day = calender.get(Calendar.DAY_OF_MONTH)


        val select = DatePickerDialog.OnDateSetListener{ datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
            val sim = SimpleDateFormat("d/M/y")
            val dateSelect = Calendar.getInstance()
            dateSelect.set(i , i1 , i2)
            edtBirthday.setText(sim.format(dateSelect.time))
        }

        DatePickerDialog(
            context!!,
            select, year, month, day
        ).show()
    }

    fun checkValidate() : Boolean{
        var b = true
        if(edtFullName.text.toString().isEmpty()) {
            textInputLayoutFullName.error = getString(R.string.error_empty)
            b = false
        }
        if(edtBirthday.text.toString().isEmpty()) {
            textInputLayoutBirthday.error = getString(R.string.error_empty)
            b = false
        }
        if(edtGender.text.toString().isEmpty()){
            textInputLayoutGender.error = getString(R.string.error_empty)
            b = false
        }
        return b
    }

    override fun resultRegisterAccount(idUser: Int?) {
        Log.d("resultRegisterAccount" , idUser.toString())
        if(idUser == null) {
           return
        }
        else
            Toast.makeText(context , R.string.fail_again , Toast.LENGTH_LONG).show()

        if(customer != null){
            customer!!.idCustomer = idUser
            if(customer!!.phoneNumber != null) {
                callActivityPhone?.result(customer!!)
                context!!.getSharedPreferences("Login" , Context.MODE_PRIVATE).edit()
                    .putString("phone" , customer!!.phoneNumber).apply()
            }

            if(customer!!.email != null) {
                callActivity?.popAllBackStack(customer!!, 1)
                context!!.getSharedPreferences("Login" , Context.MODE_PRIVATE).edit()
                    .putString("email" , customer!!.email).apply()
            }
        }
    }

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
    }

    override fun resultExistAccount(email : String? , id_fb : String? , phone : String?) {}

    override fun resultLoginAccount(customer: Customer?) {}

    override fun resultLoginPhone(customer: Customer?) {}

    override fun resultLoginFacebook(customer: Customer?) {}
}