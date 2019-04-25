package com.example.qthien.t__t.view.account

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.qthien.t__t.FragmentChosseGenderBottom
import com.example.qthien.t__t.R
import com.example.qthien.t__t.view.main.ChosseMethodPictureFragment
import com.example.qthien.t__t.view.main.EnterEmailPhoneActivity
import kotlinx.android.synthetic.main.activity_customer.*

class CustomerActivity : AppCompatActivity() , FragmentChosseGenderBottom.FragmentBottomGenderListener ,
ChosseMethodPictureFragment.ChosseMethodListener{

    private val REQUEST_CODE_CHANGE_INFO: Int = 102
    private val REQUEST_PICK_IMAGE: Int = 100
    private val REQUEST_CAMERA : Int = 101
    private val REQUEST_PERMISSION_PICK = 103
    private val REQUEST_PERMISSION_CAMERA = 104

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)

        ibtnEditAndOk.setOnClickListener({
            if(ibtnCancel.visibility == View.GONE){
                setCheckEdit()
            }
            else{
                setCheckComplete()
            }
        })

        ibtnCancel.setOnClickListener({
            setCheckComplete()
        })

        loadEvenPhoneNumber()
        loadEventEdtGender()
        loadEventEdtEmail()
        loadEventEdtPass()
    }

    fun loadEvenPhoneNumber(){
        edtPhoneInfo.setOnClickListener({
            val i = Intent(this , EnterEmailPhoneActivity::class.java)
            i.putExtra("title" , getString(R.string.change_phone_number))
            i.putExtra("txt_title" , getString(R.string.enter_your_phone_number))
            i.putExtra("hint" , getString(R.string.phone_number))
            startActivityForResult(i , REQUEST_CODE_CHANGE_INFO)
        })
    }

    fun loadEventEdtGender(){
        edtGenderInfo.setOnClickListener({
            val fragmentChosseGenderBottom = FragmentChosseGenderBottom()
            fragmentChosseGenderBottom.show(supportFragmentManager , "dialog")
        })
    }

    fun loadEventEdtEmail(){

    }

    fun loadEventEdtPass(){

    }

    fun setCheckEdit(){
        ibtnEditAndOk.setImageResource(R.drawable.ic_check_white_22dp)
        ibtnCancel.visibility = View.VISIBLE
        imgCamera.visibility = View.VISIBLE

        edtFullNameInfo.setSelection(edtFullNameInfo.text.length)

        setPropertyEditText(edtFullNameInfo , true , Color.WHITE)
        setPropertyEditText(edtGenderInfo , true , Color.WHITE)
        setPropertyEditText(edtEmailInfoo , true , Color.WHITE)
        setPropertyEditText(edtPassInfoo , true , Color.WHITE)
        setPropertyEditText(edtPhoneInfo , true , Color.WHITE)

        imgAvata.setOnClickListener({
            showMethodPicture()
        })
    }

    fun showMethodPicture(){
        ChosseMethodPictureFragment().show(supportFragmentManager , "dialog")
    }

    fun intentPickPicture(){
        val intentChosse = Intent(Intent.ACTION_GET_CONTENT)
        intentChosse.setType("image/*")
        startActivityForResult(Intent.createChooser(intentChosse, "Select Picture"), REQUEST_PICK_IMAGE)
    }

    fun intentCamera(){
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentCamera , REQUEST_CAMERA)
    }


    private fun checkPermissionChangeAvata(permission : String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callIntentbyPermission(permission)
            return
        }

        if(checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(permission) ,  getRequestCodePermission(permission))
        else
        {
            callIntentbyPermission(permission)
        }
    }

    private fun callIntentbyPermission(permission: String) {
        if(permission == Manifest.permission.READ_EXTERNAL_STORAGE)
            return intentPickPicture()
        else
            return intentCamera()
    }

    val getRequestCodePermission : (String) -> Int = {
        if (Manifest.permission.READ_EXTERNAL_STORAGE == it) REQUEST_PERMISSION_PICK
        else REQUEST_PERMISSION_CAMERA;
    }

    fun setCheckComplete(){
        ibtnCancel.visibility = View.GONE
        imgCamera.visibility = View.GONE
        ibtnEditAndOk.setImageResource(R.drawable.ic_edit)

        setPropertyEditText(edtFullNameInfo , false , ContextCompat.getColor( this, R.color.color_grey_light))
        setPropertyEditText(edtGenderInfo , false , ContextCompat.getColor( this, R.color.color_grey_light))
        setPropertyEditText(edtEmailInfoo , false , ContextCompat.getColor( this, R.color.color_grey_light))
        setPropertyEditText(edtPassInfoo , false , ContextCompat.getColor( this, R.color.color_grey_light))
        setPropertyEditText(edtPhoneInfo , false , ContextCompat.getColor( this, R.color.color_grey_light))

        imgAvata.setOnClickListener({})
    }

    fun setPropertyEditText(edt: EditText, enable: Boolean , colorEdt : Int) {
        edt.isEnabled = enable
        edt.setBackgroundColor(colorEdt)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && data != null)
            when(requestCode){
                REQUEST_PICK_IMAGE ->{
                    val uri = data.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver , uri)
                    imgAvata.setImageBitmap(bitmap)
                }

                else -> {
                    val bitmap = data.extras?.get("data") as Bitmap
                    imgAvata.setImageBitmap(bitmap)
                }
            }
    }


    override fun resultChosseGender(b: Boolean) {
        val gender = when(b){
            true -> R.string.man
            else -> R.string.woman
        }
        edtGenderInfo.setText(gender)
    }

    override fun methodSelected(method: Int) {
        if(method == 0)
            checkPermissionChangeAvata(Manifest.permission.READ_EXTERNAL_STORAGE)
        else
            checkPermissionChangeAvata(Manifest.permission.CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.size < 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this , R.string.permission_deny, Toast.LENGTH_SHORT).show()
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
