package com.example.qthien.t__t.mvp.view.branch

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.BranchAdapter
import com.example.qthien.t__t.adapter.BranchRootAdapter
import com.example.qthien.t__t.model.BranchFolowArea
import com.example.qthien.t__t.mvp.presenter.branch.PreBranch
import kotlinx.android.synthetic.main.activity_branch.*

class BranchActivity : AppCompatActivity() , IBranchActivity , BranchAdapter.BranchCallActivity {

    override fun getBranchFolow(arrBranch: ArrayList<BranchFolowArea>?) {
        if(arrBranch != null)
            this.arrBranch.addAll(arrBranch)
        else
            Toast.makeText(this , R.string.fail_again , Toast.LENGTH_LONG).show()
        adapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun failureBranch(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun startCallPhone(phone : String) {
        this.phone = phone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(
                        arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_CALL_PHONE)
            }
            else
                startIntentCallPhone(phone)
        }
        else
            startIntentCallPhone(phone)
    }

    lateinit var adapter : BranchRootAdapter
    lateinit var arrBranch : ArrayList<BranchFolowArea>
    val REQUEST_CALL_PHONE = 1
    var phone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch)

        setSupportActionBar(toolbarBranch)
        toolbarBranch.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.setTitle(R.string.branch)

        arrBranch = ArrayList()

        adapter = BranchRootAdapter(this , arrBranch)
        val layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recy_branch.layoutManager = layoutManager
        recy_branch.adapter = adapter

        swipeRefreshLayout.isRefreshing = true
        PreBranch(this).getBranchFolowArea()

        swipeRefreshLayout.setOnRefreshListener {
            PreBranch(this).getBranchFolowArea()
        }
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        onBackPressed()
        super.supportNavigateUpTo(upIntent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(REQUEST_CALL_PHONE == requestCode){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startIntentCallPhone(phone)
            }
            else
                openDialogPermisstion()        }
    }

    fun openDialogPermisstion(){
        val alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.noti)
        alert.setMessage(R.string.noti_call_permisstion)
        alert.setPositiveButton(R.string.settings , { dialog, which ->
            openSetting()
        })
        alert.setNegativeButton(R.string.close , { dialog, which ->

        })
        alert.show()
    }

    fun openSetting(){
        val intent = Intent()
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", getPackageName(), null)
        intent.setData(uri)
        startActivity(intent)
    }

    fun startIntentCallPhone(phone : String){
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phone")
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
