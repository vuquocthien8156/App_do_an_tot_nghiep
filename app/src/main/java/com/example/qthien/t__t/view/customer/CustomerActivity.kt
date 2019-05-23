package com.example.qthien.t__t.view.customer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.qthien.t__t.FragmentChosseGenderBottom
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.presenter.pre_customer.PreCustomerActivity
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.example.qthien.t__t.view.main.ChosseMethodPictureFragment
import com.example.qthien.t__t.view.main.MainActivity
import com.example.qthien.week3_ryder.GlideApp
import kotlinx.android.synthetic.main.activity_customer.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CustomerActivity : AppCompatActivity(), FragmentChosseGenderBottom.FragmentBottomGenderListener,
    ChosseMethodPictureFragment.ChosseMethodListener, IViewCustomerActivity {

    override fun resultUploadImage(fileName: String?) {
        if (!fileName.equals("null"))
            uploadInfo(fileName)
        else
            Toast.makeText(this, R.string.fail_again, Toast.LENGTH_LONG).show()
    }

    override fun failure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        progressUpdate.visibility = View.GONE
    }

    override fun resultUpdateUser(status: String?) {
        if (status.equals("ok")) {
            Toast.makeText(this, com.example.qthien.t__t.R.string.update_info_success, Toast.LENGTH_LONG).show()
            customer = customerUpdate
            MainActivity.customer = customer
            updateComplete()
        } else
            Toast.makeText(this, com.example.qthien.t__t.R.string.update_info_fail, Toast.LENGTH_LONG).show()
        progressUpdate.visibility = View.GONE
    }

    private val REQUEST_CODE_CHANGE_INFO: Int = 102
    private val REQUEST_PICK_IMAGE: Int = 100
    private val REQUEST_CAMERA: Int = 101
    private val REQUEST_PERMISSION_PICK = 103
    private val REQUEST_PERMISSION_CAMERA = 104

    var real_path = ""

    var customer: Customer? = null
    var customerFB: Customer? = null
    var customerUpdate: Customer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_customer)

        customer = MainActivity.customer
        if (customer != null) {
            customerFB = MainActivity.customerFB!!
            txtFullName.setText(customer!!.nameCustomer)

            if(customer?.avatar == null) {
                if(customerFB?.avatar == null)
                    GlideApp.with(this).load("${RetrofitInstance.baseUrl}/images/logo1.png")
                        .into(imgAvata)
                else
                    GlideApp.with(this).load(customerFB!!.avatar)
                        .into(imgAvata)
            }else
                GlideApp.with(this).load(RetrofitInstance.baseUrl + "/" + customer?.avatar)
                    .into(imgAvata)
        }

        ibtnEditAndOk.setOnClickListener({
            if (ibtnCancel.visibility == View.GONE) {
                setCheckEdit()
            } else {
                progressUpdate.visibility = View.VISIBLE
                setCheckComplete()
            }
        })

        ibtnCancel.setOnClickListener({
            updateComplete()
        })

        loadEvenPhoneNumber()
        loadEventEdtGender()
        loadEventEdtPass()

        loadData()
    }

    @SuppressLint("SimpleDateFormat")
    fun loadData() {
        val notUpdate = getString(R.string.not_update)

        edtFullNameInfo.setText(customer?.nameCustomer)
        edtPhoneInfo.setText(customer?.phoneNumber ?: notUpdate)

        var birthday = customer?.birthday
        if (birthday != null) {
            var arr = birthday.split("/")

            if (birthday.contains("-")) {
                arr = birthday.split("-")
            }

            birthday = "${arr[2]}/${arr[1]}/${arr[0]}"
            edtBirthdayInfo.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            edtBirthdayInfo.setOnClickListener({})
        } else {
            birthday = notUpdate
            edtBirthdayInfo.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                getDrawable(R.drawable.ic_expand_up),
                null
            )
            edtBirthdayInfo.setOnClickListener({
                selectDate()
            })
        }
        edtBirthdayInfo.setText(birthday)

        val gender = when (customer?.gender) {
            0 -> getString(R.string.man)
            1 -> getString(R.string.woman)
            else -> notUpdate
        }
        edtGenderInfo.setText(gender)

        if (customer?.email == null) {
            lnInfoEmail.visibility = View.GONE
            lnInfoPassword.visibility = View.GONE
        } else
            edtEmailInfoo.setText(customer?.email)
    }

    @SuppressLint("SimpleDateFormat")
    fun selectDate() {
        var year = 0
        var month = 0
        var day = 0
        val calender = Calendar.getInstance()
        Log.d("thienvip123", edtBirthdayInfo.getText().contains("[").toString())
        if (edtBirthdayInfo.getText()?.length != 0 && !edtBirthdayInfo.getText().contains("[")) {
            val simpleDateFormat = SimpleDateFormat("d/M/y")
            calender.time = simpleDateFormat.parse(edtBirthdayInfo.getText().toString())
        }

        year = calender.get(Calendar.YEAR)
        month = calender.get(Calendar.MONTH)
        day = calender.get(Calendar.DAY_OF_MONTH)


        val select = DatePickerDialog.OnDateSetListener { datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
            val sim = SimpleDateFormat("d/M/y")
            val dateSelect = Calendar.getInstance()
            dateSelect.set(i, i1, i2)
            edtBirthdayInfo.setText(sim.format(dateSelect.time))
        }

        DatePickerDialog(
            this,
            select, year, month, day
        ).show()
    }


    fun loadEvenPhoneNumber() {
        edtPhoneInfo.setOnClickListener({
            val i = Intent(this, EnterEmailPhoneActivity::class.java)
            i.putExtra("title", getString(com.example.qthien.t__t.R.string.change_phone_number))
            i.putExtra("txt_title", getString(com.example.qthien.t__t.R.string.enter_your_phone_number))
            i.putExtra("hint", getString(com.example.qthien.t__t.R.string.phone_number))
            i.putExtra("type" , "phone")
            startActivityForResult(i, REQUEST_CODE_CHANGE_INFO)
        })
    }

    fun loadEventEdtGender() {
        edtGenderInfo.setOnClickListener({
            val fragmentChosseGenderBottom = FragmentChosseGenderBottom()
            fragmentChosseGenderBottom.show(supportFragmentManager, "dialog")
        })
    }

    fun loadEventEdtPass() {

    }

    fun setCheckEdit() {

        ibtnEditAndOk.setImageResource(com.example.qthien.t__t.R.drawable.ic_check_white_22dp)
        ibtnCancel.visibility = View.VISIBLE
        edtFullNameInfo.setSelection(edtFullNameInfo.text.length)

        setPropertyEditText(edtFullNameInfo, true, Color.WHITE)
        setPropertyEditText(edtGenderInfo, true, Color.WHITE)
        setPropertyEditText(edtEmailInfoo, true, Color.WHITE)
        if (customer?.birthday == null)
            setPropertyEditText(edtBirthdayInfo, true, Color.WHITE)
        setPropertyEditText(edtPassInfoo, true, Color.WHITE)
        setPropertyEditText(edtPhoneInfo, true, Color.WHITE)

        imgCamera.visibility = View.VISIBLE
        imgAvata.setOnClickListener({
            showMethodPicture()
        })
    }

    fun showMethodPicture() {
        ChosseMethodPictureFragment().show(supportFragmentManager, "dialog")
    }

    fun intentPickPicture() {
        val intentChosse = Intent(Intent.ACTION_PICK)
        intentChosse.setType("image/*")
        startActivityForResult(Intent.createChooser(intentChosse, "Select Picture"), REQUEST_PICK_IMAGE)
    }

    fun intentCamera() {
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentCamera, REQUEST_CAMERA)
    }


    private fun checkPermissionChangeAvata(permission: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callIntentbyPermission(permission)
            return
        }

        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(permission), getRequestCodePermission(permission))
        else {
            callIntentbyPermission(permission)
        }
    }

    private fun callIntentbyPermission(permission: String) {
        if (permission == Manifest.permission.READ_EXTERNAL_STORAGE)
            return intentPickPicture()
        else
            return intentCamera()
    }

    val getRequestCodePermission: (String) -> Int = {
        if (Manifest.permission.READ_EXTERNAL_STORAGE == it) REQUEST_PERMISSION_PICK
        else REQUEST_PERMISSION_CAMERA;
    }

    fun updateComplete() {
        ibtnCancel.visibility = View.GONE
        imgCamera.visibility = View.GONE
        ibtnEditAndOk.setImageResource(com.example.qthien.t__t.R.drawable.ic_edit)

        setPropertyEditText(
            edtFullNameInfo,
            false,
            ContextCompat.getColor(this, com.example.qthien.t__t.R.color.color_grey_light)
        )
        setPropertyEditText(
            edtGenderInfo,
            false,
            ContextCompat.getColor(this, com.example.qthien.t__t.R.color.color_grey_light)
        )
        setPropertyEditText(
            edtEmailInfoo,
            false,
            ContextCompat.getColor(this, com.example.qthien.t__t.R.color.color_grey_light)
        )
        setPropertyEditText(
            edtPassInfoo,
            false,
            ContextCompat.getColor(this, com.example.qthien.t__t.R.color.color_grey_light)
        )
        setPropertyEditText(
            edtPhoneInfo,
            false,
            ContextCompat.getColor(this, com.example.qthien.t__t.R.color.color_grey_light)
        )
        setPropertyEditText(
            edtBirthdayInfo,
            false,
            ContextCompat.getColor(this, com.example.qthien.t__t.R.color.color_grey_light)
        )

        loadData()
        imgAvata.setOnClickListener({})
    }

    fun setCheckComplete() {
        var file: File? = null
        if (!real_path.equals("")) {
            file = File(real_path)
            PreCustomerActivity(this).uploadImage(file)
        } else
            uploadInfo(MainActivity.customer!!.avatar)
    }

    fun uploadInfo(urlImage: String?) {

        val birthday: String = edtBirthdayInfo.text.toString()
        val name: String = edtFullNameInfo.text.toString()
        val phone: String = edtPhoneInfo.text.toString()
        val gender: String = edtGenderInfo.text.toString()

        customerUpdate = Customer(
            customer!!.idCustomer,
            customer!!.id_fb,
            if (name.trim().equals("")) MainActivity.customer?.nameCustomer else name,
            if (phone.contains("[")) null else phone,
            if (!gender.contains("[")) if (gender.equals("Nam")) 0 else 1 else null,
            customer!!.point,
            if (!birthday.contains("[")) edtBirthdayInfo.text.toString() else null,
            edtEmailInfoo.text.toString(),
            customer!!.address,
            urlImage,
            null
        )

        PreCustomerActivity(this).uploadInfoCustomer(customerUpdate!!)
    }

    @SuppressLint("Recycle")
    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } catch (e: Exception) {
            Log.e("getRealPathFromURI", "getRealPathFromURI Exception : $e")
            return ""
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    fun setPropertyEditText(edt: EditText, enable: Boolean, colorEdt: Int) {
        edt.isEnabled = enable
        edt.setBackgroundColor(colorEdt)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null)
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    val uri = data.data
                    real_path = getRealPathFromURI(this, uri)
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    imgAvata.setImageBitmap(bitmap)
                }

                REQUEST_CAMERA -> {
                    real_path = getRealPathFromURI(this, data.data)
                    val bitmap = data.extras?.get("data") as Bitmap
                    imgAvata.setImageBitmap(bitmap)
                }

                REQUEST_CODE_CHANGE_INFO ->{
                    val phone = data.extras!!.getString("phoneResult")
                    edtPhoneInfo.setText(phone)
                }
            }
    }


    override fun resultChosseGender(b: Boolean) {
        val gender = when (b) {
            true -> com.example.qthien.t__t.R.string.man
            else -> com.example.qthien.t__t.R.string.woman
        }
        edtGenderInfo.setText(gender)
    }

    override fun methodSelected(method: Int) {
        if (method == 0)
            checkPermissionChangeAvata(Manifest.permission.READ_EXTERNAL_STORAGE)
        else
            checkPermissionChangeAvata(Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.size < 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, com.example.qthien.t__t.R.string.permission_deny, Toast.LENGTH_SHORT).show()
            return
        }

        when (requestCode) {
            REQUEST_PERMISSION_PICK -> {
                intentPickPicture()
            }

            REQUEST_PERMISSION_CAMERA -> {
                intentPickPicture()
            }
        }
    }
}
