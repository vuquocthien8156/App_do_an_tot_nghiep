package com.example.qthien.t__t.view.ealuation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.RatingBar
import android.widget.Toast
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.ImageSelectedAdapter
import com.example.qthien.t__t.model.Evaluation
import com.example.qthien.t__t.presenter.detail_product.PreEvaluation
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.example.qthien.t__t.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_create_evaluation.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CreateEvaluationActivity : AppCompatActivity() , ImageSelectedAdapter.ImagesAdapterCommunication ,
        IEvaluation{
    override fun successAddImg(arrUrl : ArrayList<String>) {
        if(arrUrl.size > 0)
            addEv(arrUrl)
        else
            Toast.makeText(this, R.string.fail_again,
                    Toast.LENGTH_LONG).show()
    }

    override fun successAddEvaluation(status: String?) {
        Log.d("adddddEv" , status.toString())
        if(status != null && status.equals("Success")){
            val alert = AlertDialog.Builder(this)
            alert.setCancelable(false)
            alert.setTitle(R.string.noti)
            alert.setMessage(R.string.success_evaluate_check_after)
            alert.setPositiveButton(R.string.know , { dialog, which ->
                finish()
            })
            val dialog = alert.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
        else {
            Toast.makeText(this, R.string.fail_again,
                    Toast.LENGTH_LONG).show()
        }
    }

    override fun failure(message: String) {
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show()
    }

    override fun removeImage(position: Int) {
        arrStringPath.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    val REQUEST_PICK_IMAGE = 1
    val REQUEST_CAMERA = 2
    lateinit var adapter : ImageSelectedAdapter
    lateinit var arrStringPath : ArrayList<String>
    lateinit var arrStringRating : ArrayList<String>
    var idProduct : Int? = 0
    var REQUEST_PERMISSTION_CAMERA = 3
    var REQUEST_PERMISSTION_PICK = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.evaluate_product)
        setContentView(com.example.qthien.t__t.R.layout.activity_create_evaluation)

        setSupportActionBar(toolbarAddEvaluate)
        toolbarAddEvaluate.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        val url = intent.extras?.getString("url")
        val name = intent.extras?.getString("name")
        idProduct = intent.extras?.getInt("id")

        GlideApp.with(this).load("${RetrofitInstance.baseUrl}/$url").into(imgProduct)
        txtNameProduct.setText(name)

        imgAddPhotoCamera.setOnClickListener({
            checkPermistion(Manifest.permission.CAMERA , intentCamera , REQUEST_PERMISSTION_CAMERA)
        })

        imgAddPhoto.setOnClickListener({
            checkPermistion(Manifest.permission.READ_EXTERNAL_STORAGE , intentPickImages, REQUEST_PERMISSTION_PICK)
        })

        arrStringRating = arrayListOf(getString(R.string.not_rating) , getString(R.string.dissatisfaction) , getString(R.string.unsatisfied) ,
                getString(R.string.normal),getString(R.string.satisfied) , getString(R.string.very_satisfied))

        arrStringPath = ArrayList()
        adapter = ImageSelectedAdapter(this , arrStringPath)
        recyImageSelected.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false)
        recyImageSelected.setAdapter(adapter)

        setUpRatingBar()
        setUpEdtContentEv()
        setUpBtnAddEv()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setUpBtnAddEv() {
        btnSendEv.setOnClickListener({
            if(arrStringPath.size > 0){
                val arrFile : ArrayList<File> = ArrayList()
                for(filePath in arrStringPath)
                    arrFile.add(File(filePath))
                PreEvaluation(this).addImagesEvaluate(arrFile)
            }
            else
                addEv(ArrayList())
        })
    }

    fun addEv(arrUrl: ArrayList<String>) {
        val evAdd = Evaluation(MainActivity.customer!!.idCustomer
                ,0, "",
                idProduct!!,
                ratingProduct.rating.toInt(),
                if (edtTileEv.text.isNullOrEmpty()) txtNameRating.text.toString() else edtTileEv.text.toString(),
                edtContentEv.text.toString(), SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time),
                0, 0, arrUrl , arrayListOf())
        PreEvaluation(this).addEvaluate(evAdd)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun setUpEdtContentEv(){
        edtContentEv.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s != null && s.length > 4000)
                    inputlayoutContent.setError(getString(R.string.error_edt_content_ev))
                else
                    inputlayoutContent.setError(null)

                checkEnableBtnSend()
            }
        })
    }

    fun checkEnableBtnSend(){
        val length = edtContentEv.text.toString().length
        if(ratingProduct.rating.toInt() != 0 && length >= 20 && length <= 4000 ){
            btnSendEv.isEnabled = true
            btnSendEv.setTextColor(Color.WHITE)
        }
        else {
            btnSendEv.isEnabled = false
            btnSendEv.setTextColor(ContextCompat.getColor(this@CreateEvaluationActivity , R.color.colortext_un_endable_btn)
            )
        }
    }

    fun setUpRatingBar(){
        txtNameRating.setText(arrStringRating.get(0))
        txtNameRating.setTextColor(ContextCompat.getColor(this@CreateEvaluationActivity , R.color.colorPrimary))

        ratingProduct.onRatingBarChangeListener = object : RatingBar.OnRatingBarChangeListener{
            override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
                txtNameRating.setText(arrStringRating.get(rating.toInt()))
                if(rating.toInt() == 0)
                    txtNameRating.setTextColor(ContextCompat.getColor(this@CreateEvaluationActivity , R.color.colorPrimary))
                else
                    txtNameRating.setTextColor(ContextCompat.getColor(this@CreateEvaluationActivity , R.color.colorAccent))
                checkEnableBtnSend()
            }
        }
    }

    fun checkPermistion(permission: String , startIntent : () -> Unit , requestCode: Int){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startIntent()
            return
        }

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(permission), requestCode)
        else {
            startIntent()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUEST_PERMISSTION_CAMERA ->
                checkPermistion(Manifest.permission.CAMERA , intentCamera , REQUEST_PERMISSTION_CAMERA)
            REQUEST_PERMISSTION_PICK ->
                checkPermistion(Manifest.permission.READ_EXTERNAL_STORAGE , intentPickImages, REQUEST_PERMISSTION_PICK)
        }
    }

    val intentPickImages = fun(){
        val intentChosse = Intent(this , SelectedImgActivity::class.java)
        startActivityForResult(intentChosse, REQUEST_PICK_IMAGE)
    }

    val intentCamera = fun(){
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentCamera, REQUEST_CAMERA)
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            // When an Image is picked
            if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                val arrUrl = data.extras?.getStringArrayList("arrImage")
                    if (arrUrl != null && (arrUrl.size + arrStringPath.size) <= 3) {
                            arrStringPath.addAll(arrUrl)
                    }
                    else{
                        val alert = AlertDialog.Builder(this)
                        alert.setTitle(R.string.noti)
                        alert.setMessage(R.string.error_selected_img)
                        alert.setPositiveButton(R.string.know , { dialog, which ->
                            dialog.dismiss()
                        })
                    }

                adapter.notifyDataSetChanged()
            }


            if(requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && null != data){
                val uri = data.data
                val realPath = getRealPathFromURI(applicationContext , uri!!)
                arrStringPath.add(realPath)
                adapter.notifyDataSetChanged()
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
