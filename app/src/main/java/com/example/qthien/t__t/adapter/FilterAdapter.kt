package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.qthien.t__t.R
import kotlinx.android.synthetic.main.layout_filter_menu.view.*

class FilterAdapter(var context: Context, var arrString: MutableList<String?>) : BaseAdapter() {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_filter_menu , parent , false)
        val txt = view.txt

        val s = arrString.get(position)
        txt.setText(s)
        return view
    }

    override fun getItem(position: Int): String? = arrString.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = arrString.size

}