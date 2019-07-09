package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Evaluation
import com.example.qthien.t__t.mvp.view.ealuation.DetailEvaluationActivity
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import kotlinx.android.synthetic.main.item_recycler_evaluation.view.*
import java.text.SimpleDateFormat

class EvaluationAdapter(var context : Context,var arrEvaluation : ArrayList<Evaluation>) : RecyclerView.Adapter<EvaluationAdapter.ViewHolder>() {

    interface EvaluationAdapterCall{
        fun addTks(idEvluated : Int)
    }

    var evaluationAdapterCall : EvaluationAdapterCall? = null

    init {
        if(context is EvaluationAdapterCall)
            evaluationAdapterCall = context as EvaluationAdapterCall
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_evaluation , p0 , false))

    override fun getItemCount(): Int = arrEvaluation.size

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
        val evalu = arrEvaluation.get(p1)

        vh.txtTitle.setText(evalu.title)
        val sim = SimpleDateFormat("dd/mm/yyyy")
        vh.txtTime.setText(sim.format(SimpleDateFormat("yyyy-mm-dd").parse(evalu.time)))
        vh.txtNameCus.setText(evalu.nameCustomer)
        vh.txtContent.setText(evalu.content)
        vh.ratingbar.numStars = evalu.point



        val tks = if (evalu.quantityTks != 0) evalu.quantityTks.toString() else ""
        vh.txtTks.setText("$tks ${context.getString(R.string.thanks)}")

        if (checkTks(evalu.idEvaluation)) {
            vh.txtTks.isEnabled = false
            vh.txtTks.setTextColor(Color.parseColor("#27642A"))
        } else{
            vh.txtTks.isEnabled = true
            vh.txtTks.setTextColor(Color.parseColor("#00C20D"))
        }

        if(evalu.isOrder == 0){
            vh.txtIsOrder.setText(R.string.not_order)
            vh.txtIsOrder.setTextColor(ContextCompat.getColor(context , R.color.colorPrimary))
        }
        else {
            vh.txtIsOrder.setTextColor(ContextCompat.getColor(context , R.color.green_compelete))
            vh.txtIsOrder.setText(R.string.is_order)
        }

        val baseUrl = RetrofitInstance.baseUrl
        when(evalu.images.size){
            0 ->{
                vh.img3.visibility = View.GONE
                vh.img1.visibility = View.GONE
                vh.img2.visibility = View.GONE
            }
            1 -> {
                GlideApp.with(context).load("$baseUrl/${evalu.images[0]}").into(vh.img2)
                vh.img3.visibility = View.GONE
                vh.img1.visibility = View.GONE
            }
            2 -> {
                GlideApp.with(context).load("$baseUrl/${evalu.images[0]}").into(vh.img2)
                GlideApp.with(context).load("$baseUrl/${evalu.images[1]}").into(vh.img1)
                vh.img3.visibility = View.GONE
            }
            3 -> {
                GlideApp.with(context).load("$baseUrl/${evalu.images[0]}").into(vh.img2)
                GlideApp.with(context).load("$baseUrl/${evalu.images[1]}").into(vh.img1)
                GlideApp.with(context).load("$baseUrl/${evalu.images[2]}").into(vh.img3)
            }
        }

        vh.recyclerChild.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        vh.recyclerChild.adapter = EvaluationChildAdapter(context , evalu.childs)

        vh.txtReply.setOnClickListener({
            val i = Intent(context , DetailEvaluationActivity::class.java)
            i.putExtra("Evaluation" , evalu)
            context.startActivity(i)
        })

        vh.txtTks.setOnClickListener({
            if(evaluationAdapterCall != null)
                evaluationAdapterCall?.addTks(evalu.idEvaluation)
        })
    }

    fun checkTks(idEvluated: Int) : Boolean{
        val share =  context.getSharedPreferences("EvaluateTks" , Context.MODE_PRIVATE)
        val setTks = share.getStringSet("setTks" , null)
        if(setTks?.find { it.equals("$idEvluated") } != null)
            return true
        return false
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyclerChild = itemView.recyclerReply
        val txtReply = itemView.txtReply
        val txtTks = itemView.txtthanks
        val txtTitle = itemView.txtTitleEvaluation
        val txtTime = itemView.txtTimeEvaluation
        val txtNameCus = itemView.txtNameUserEvaluation
        val ratingbar = itemView.ratingEvaluation
        val txtContent = itemView.txtContentValuation
        val txtIsOrder= itemView.txtIsOrder
        val img1 = itemView.imgEvaluation1
        val img2 = itemView.imgEvaluation2
        val img3 = itemView.imgEvaluation3
    }
}