package com.example.qthien.t__t.view.login

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import com.example.qthien.t__t.FragmentChosseGenderBottom
import com.example.qthien.t__t.R
import kotlinx.android.synthetic.main.fragment_enter_info.*
import kotlinx.android.synthetic.main.fragment_enter_info.view.*
import java.text.SimpleDateFormat
import java.util.*

class EnterInfoFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.enter_info)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

            }
            else{

            }
        })

        // event text change
        val textWatcher = object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(!edtFullName.text.toString().isEmpty()) {
                    textInputLayoutFullName.isErrorEnabled = false
                }
                if(!edtBirthday.text.toString().isEmpty()) {
                    textInputLayoutBirthday.isErrorEnabled = false
                }
                if(edtGender.text.toString().isEmpty()){
                    textInputLayoutGender.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }

        edtFullName.addTextChangedListener(textWatcher)
        edtBirthday.addTextChangedListener(textWatcher)
        edtGender.addTextChangedListener(textWatcher)
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
}