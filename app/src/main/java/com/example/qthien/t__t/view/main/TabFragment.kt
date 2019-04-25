package com.example.qthien.t__t.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.ListProductAdapter
import com.example.qthien.t__t.adapter.ProductsAdapter
import kotlinx.android.synthetic.main.fragment_tab.*

class TabFragment : Fragment() {
    lateinit var adapterProduct : ProductsAdapter
    lateinit var adapterListproduct : ListProductAdapter
    companion object {
        fun newInstance(type : Int) : TabFragment {
            val bundle = Bundle()
            bundle.putInt("type" , type)
            val tabFragment = TabFragment()
            tabFragment.arguments = bundle
            return tabFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_tab , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments?.getInt("type")

        when(type){
            1 ->{
                adapterProduct = ProductsAdapter(context!! , arrayListOf(""))
                val layoutManager = GridLayoutManager(context , 2)
                recylerProduct.layoutManager = layoutManager
                recylerProduct.adapter = adapterProduct
                Log.d("Adapterrrr" , "1")
            }
            else ->{
                adapterListproduct = ListProductAdapter(context!! , arrayListOf(""))
                val layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
                recylerProduct.layoutManager = layoutManager
                recylerProduct.adapter = adapterListproduct
                Log.d("Adapterrrr" , "2")
            }
        }

    }
}