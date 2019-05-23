package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.AddressDelivery
import kotlinx.android.synthetic.main.item_recy_address_delivery.view.*

class DeliveryAdapterAddress(var context : Context, var arrAdress : ArrayList<AddressDelivery>)
    : RecyclerView.Adapter<DeliveryAdapterAddress.ViewHolder>() {

    interface AddressCommunication{
        fun selectAddress(position: Int)
        fun remove(position: Int)
        fun edit(position: Int)
    }

    var addressCommunication : AddressCommunication? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_address_delivery , p0 , false))

    override fun getItemCount(): Int = arrAdress.size

    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {

        if(context is AddressCommunication)
            addressCommunication = context as AddressCommunication

        val ad = arrAdress.get(p1)
        vh.txtName.setText(ad.nameCustomer)
        vh.txtPhone.setText(ad.phoneNumber)
        vh.txtAdd.setText(ad.addressDelivery)

        if(ad.main == 1){
            vh.txtDefalt.setVisibility(View.VISIBLE);
            vh.ibtnRemove.setVisibility(View.GONE);
        }
        else {
            vh.txtDefalt.setVisibility(View.GONE);
            vh.ibtnRemove.setVisibility(View.VISIBLE);
        }

        vh.ibtnEdit.setOnClickListener({
            if(addressCommunication != null)
                addressCommunication?.edit(p1)
        })

        vh.ibtnRemove.setOnClickListener({

        })
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val txtName = itemView.txtNameAdd
        val txtPhone = itemView.txtPhoneAdd
        val txtAdd = itemView.txtAddressAdd
        val txtDefalt = itemView.txtDefaltAdd
        val ibtnEdit = itemView.ibtnEdit
        val ibtnRemove = itemView.ibtnRemove
        val layout = itemView.layout_item_lv_select
    }

}