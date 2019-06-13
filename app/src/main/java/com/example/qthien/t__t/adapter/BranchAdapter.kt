package com.example.qthien.t__t.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.model.Branch
import kotlinx.android.synthetic.main.item_recy_branch.view.*


class BranchAdapter(var context : Context, var arrBranch : ArrayList<Branch>)
    : RecyclerView.Adapter<BranchAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(com.example.qthien.t__t.R.layout.item_recy_branch , p0 , false))

    interface BranchCallActivity{
        fun startCallPhone(phone : String)
    }

    var branchCallActivity : BranchCallActivity? = null

    init {
        if(context is BranchCallActivity)
            branchCallActivity = context as BranchCallActivity
    }

    override fun getItemCount(): Int = arrBranch.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.txtName.setText(arrBranch[p1].nameBranch)
        p0.txtAddress.setText(arrBranch[p1].addressBranch)
        p0.txtPhone.setText(arrBranch[p1].phoneNumberBranch)

        p0.txtPhone.setOnClickListener {
            branchCallActivity?.startCallPhone(arrBranch[p1].phoneNumberBranch)
        }

        p0.txtDirectory.setOnClickListener({
            val gmmIntentUri = Uri.parse("google.navigation:q=${arrBranch[p1].latitude},${arrBranch[p1].longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        })

        GlideApp.with(context)
            .load("https://noithatcaphe.vn/images/2016/06/14/thiet-ke-noi-that-quan-cafe-phong-cach-co-dien-dep-doc-dao-2.jpg")
            .into(p0.img)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.imgBranch
        val txtName = itemView.txtNameBranch
        val txtAddress = itemView.txtAddressBranch
        val txtPhone = itemView.txtPhoneBranch
        val txtDirectory = itemView.txtDirectory
    }
}