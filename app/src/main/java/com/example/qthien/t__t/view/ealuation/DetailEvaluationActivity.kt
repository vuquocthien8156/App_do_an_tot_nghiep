package com.example.qthien.t__t.view.ealuation

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.EndlessRecyclerViewScrollListener
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.EvaluationChildAdapter
import com.example.qthien.t__t.model.ChildEvaluation
import com.example.qthien.t__t.model.Evaluation
import com.example.qthien.t__t.presenter.detail_product.PreDetailEvaluation
import com.example.qthien.t__t.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_detail_evaluation.*
import java.text.SimpleDateFormat
import java.util.*

class DetailEvaluationActivity : AppCompatActivity() , IEvaluationDetail {

    override fun succressAddChild(status: String?) {
        if(status.equals("Success")){
            adapter.notifyDataSetChanged()
            edtEvaluation.setText("")
        }
        else{
            arrChildEv.removeAt(arrChildEv.size - 1)
            Toast.makeText(this , R.string.fail_again , Toast.LENGTH_LONG).show()
        }
    }

    override fun successGetAllChildEvaluation(arrChild: ArrayList<ChildEvaluation>) {
        if(arrChild.size > 0){
            arrChildEv.addAll(arrChild)
            if(arrChildEv.size == 1)
                adapter.notifyDataSetChanged()
            else
                adapter.notifyItemRangeInserted(adapter.itemCount , arrChild.size - 1)
        }

        progressLoader.visibility = View.GONE
    }

    override fun failure(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    lateinit var adapter : EvaluationChildAdapter
    lateinit var arrChildEv : ArrayList<ChildEvaluation>
    var pagee = 1
    var arrKeyword = arrayListOf("lz" , "ccc" , "cac" , "lon")

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_evaluation)
        arrChildEv = ArrayList()
        adapter = EvaluationChildAdapter(this , arrChildEv)
        val layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerChild.layoutManager = layoutManager
        recyclerChild.adapter = adapter
        recyclerChild.isNestedScrollingEnabled = false

        val evaluation : Evaluation? = intent.extras?.getParcelable("Evaluation") as Evaluation?

        val sim = SimpleDateFormat("yyyy-mm-dd")
        txtTimeEv.setText(SimpleDateFormat("dd/mm/yyyy").format(sim.parse(evaluation?.time)))
        txtTitleEvaluationDetail.setText(evaluation?.title)
        txtContentEvaluation.setText(evaluation?.content)

        PreDetailEvaluation(this@DetailEvaluationActivity).getChildEvalute(evaluation!!.idEvaluation  , pagee)

        recyclerChild.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.d("pageeeeeeeee" , page.toString())
                progressLoader.visibility = View.VISIBLE
                PreDetailEvaluation(this@DetailEvaluationActivity).getChildEvalute(evaluation.idEvaluation  , pagee + 1)
            }
        })

        ibtnSend.isEnabled = false

        ibtnSend.setOnClickListener({
            var done = 1
            for(word in arrKeyword){
                if( edtEvaluation.text.toString().contains(word))
                    done = 0
            }

            if(done == 1) {
                val childAdd = ChildEvaluation(MainActivity.customer!!.idCustomer ,0, evaluation!!.idEvaluation, MainActivity.customer!!.nameCustomer.toString() ,
                        edtEvaluation.text.toString(), SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time), done)
                arrChildEv.add(childAdd)
                Log.d("childddddd" , childAdd.time)
                PreDetailEvaluation(this).addChildEvaluate(childAdd)
            }
            else{
                val alert = AlertDialog.Builder(this)
                alert.setTitle(R.string.noti)
                alert.setMessage(R.string.message_noti_evaluate)
                alert.setPositiveButton(R.string.know , { dialog, which ->
                    dialog.dismiss()
                })
            }
        })

        edtEvaluation.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty()) {
                    ibtnSend.isEnabled = false
                    ibtnSend.setImageResource(R.drawable.ic_send_gray_24dp)
                }else {
                    ibtnSend.isEnabled = true
                    ibtnSend.setImageResource(R.drawable.ic_send_24dp)
                }
            }
        })
    }
}
