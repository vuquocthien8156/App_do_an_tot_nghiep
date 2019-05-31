package com.example.qthien.t__t.view.branch

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.BranchAdapter
import com.example.qthien.t__t.adapter.BranchRootAdapter
import com.example.qthien.t__t.model.BranchFolowArea
import com.example.qthien.t__t.presenter.branch.PreBranch
import kotlinx.android.synthetic.main.activity_branch.*

class BranchActivity : AppCompatActivity() , IBranchActivity , BranchAdapter.BranchCallActivity {

    override fun getBranchFolow(arrBranch: ArrayList<BranchFolowArea>?) {
        if(arrBranch != null)
            this.arrBranch.addAll(arrBranch)
        else
            Toast.makeText(this , R.string.fail_again , Toast.LENGTH_LONG).show()
        adapter.notifyDataSetChanged()
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

        PreBranch(this).getBranchFolowArea()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(REQUEST_CALL_PHONE == requestCode){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startIntentCallPhone(phone)
            }
            else
                Toast.makeText(this , R.string.permission_deny , Toast.LENGTH_LONG).show()
        }
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
