package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.InfoAddress
import kotlinx.android.synthetic.main.item_recy_address_delivery.view.*

class DeliveryAdapterAddress(var context : Context, var arrAdress : ArrayList<InfoAddress>)
    : RecyclerView.Adapter<DeliveryAdapterAddress.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_address_delivery , p0 , false))

    override fun getItemCount(): Int = arrAdress.size

    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {

        val ad = arrAdress.get(p1)
        vh.txtName.setText(ad.nameCustomer)
        vh.txtPhone.setText(ad.phoneCustomer)
        vh.txtAdd.setText(ad.addressInfo)

        if(ad.main == 1){
            vh.txtDefalt.setVisibility(View.VISIBLE);
        }
        else {
            vh.txtDefalt.setVisibility(View.GONE);
        }

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val txtName = itemView.txtNameAdd
        val txtPhone = itemView.txtPhoneAdd
        val txtAdd = itemView.txtAddressAdd
        val txtDefalt = itemView.txtDefaltAdd
//        val ibtnRemove = itemView.delete_task
        val layout = itemView.layout_item_lv_select
        val layoutEdit = itemView.rowBGEdit
        val layoutDelete = itemView.rowBGDelete
        val rowFG = itemView.rowFG
    }

}