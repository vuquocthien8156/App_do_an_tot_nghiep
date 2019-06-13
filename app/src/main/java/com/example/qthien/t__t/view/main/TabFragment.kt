package com.example.qthien.t__t.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qthien.t__t.adapter.ListProductAdapter
import com.example.qthien.t__t.model.CatalogyProduct
import com.example.qthien.t__t.model.InfoAddress
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.model.ProductByCatalogy
import com.example.qthien.t__t.presenter.pre_frag_order.PreFragOrder
import kotlinx.android.synthetic.main.fragment_tab.*







class TabFragment : Fragment() , IViewFragOrder {
    override fun resultGetAddressDefault(infoAddress: InfoAddress?) {}
    override fun successGetLocationAddress(address: String, latLng: com.google.android.gms.maps.model.LatLng) {}
    override fun resultGetAllCatalogy(arrCatalogy: ArrayList<CatalogyProduct>?) {}

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
        (parentFragment as OrderFragment).finishLoader()
    }

    override fun resultGetProductBestFavorite(arrProduct : ArrayList<Product>?) {
        if(arrProduct != null) {
            Log.d("resultGet" , "BestFavorite : ${arrProduct.size}")
            arrProductTab.add(ProductByCatalogy(0 ,arrProduct))
            adapterListproduct.notifyDataSetChanged()
        }
    }

    override fun resultGetProductsBestBuy(arrProduct: ArrayList<Product>?) {
        if(arrProduct != null) {
            arrProductTab.add(ProductByCatalogy(0 ,arrProduct))
            adapterListproduct.notifyDataSetChanged()
        }
    }

    override fun resultGetProductsByCatalogy(arrProduct: ArrayList<Product>?) {
        if(arrProduct != null) {
            arrProductTab.add(ProductByCatalogy(catalogyLast ,arrProduct))
            adapterListproduct.notifyItemRangeInserted( adapterListproduct.itemCount ,arrProduct.size - 1)
            index += 1
            if(index < arrCatalogy!!.size)
                preFragOrder.getProductsByCatalogy(arrCatalogy?.get(index)?.idCatalogy)
            else
                (parentFragment as OrderFragment).finishLoader()
        }
    }

    companion object {
        fun newInstance(type : Int , arrCatalogy: ArrayList<CatalogyProduct>? = null) : TabFragment {
            val bundle = Bundle()
            bundle.putInt("type" , type)
            bundle.putParcelableArrayList("arrCatalogy" , arrCatalogy)
            val tabFragment = TabFragment()
            tabFragment.arguments = bundle
            return tabFragment
        }
    }

    lateinit var adapterListproduct : ListProductAdapter
    lateinit var preFragOrder: PreFragOrder
    lateinit var arrProductTab : ArrayList<ProductByCatalogy>


    var type : Int? = 0
    var index = 0
    var catalogyLast : Int? = 0
    var arrCatalogy : ArrayList<CatalogyProduct>? = null
    var scrollBottom = false
    var scrollDown = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(com.example.qthien.t__t.R.layout.fragment_tab , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrProductTab = ArrayList()

        preFragOrder = PreFragOrder(context!! , this)

        adapterListproduct = ListProductAdapter(context!! , arrProductTab)
        val layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        recylerProduct.layoutManager = layoutManager
        recylerProduct.adapter = adapterListproduct
        recylerProduct.isNestedScrollingEnabled = false

        nested.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
//                Toast.makeText(context, "Scroll DOWN", Toast.LENGTH_LONG).show()
                (parentFragment as OrderFragment).callVisibleNavigationMain(false)
                scrollDown = true
            }
            if (scrollY < oldScrollY) {
//                Toast.makeText(context, "Scroll UP", Toast.LENGTH_LONG).show()
                (parentFragment as OrderFragment).callVisibleNavigationMain(true)
                scrollDown = false
                if(scrollBottom){
                    (parentFragment as OrderFragment).visibleBtnFilter(false)
                    scrollBottom = false
                }
            }

            if (scrollY == 0) {
                Toast.makeText(context, "TOP SCROLL", Toast.LENGTH_LONG).show()
            }

            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                scrollBottom = true
                (parentFragment as OrderFragment).visibleBtnFilter(true)
                (parentFragment as OrderFragment).callVisibleNavigationMain(false)
            }
        })

        type = arguments?.getInt("type")
        arrCatalogy = arrayListOf()


        when(type){
            1 , 2 ->{ // Thức ăn or Nước uống
                arrCatalogy = arguments?.getParcelableArrayList("arrCatalogy")
                Log.d("arrrr" , arrCatalogy.toString())
                preFragOrder.getProductsByCatalogy(arrCatalogy?.get(index)?.idCatalogy)
            }
            3 -> { // Đặt nhiều
//                preFragOrder.getProductsBestBuy()
            }
            4 -> { // Yêu thích nhiều
                preFragOrder.getProductBestFavorite()
                Log.d("resultGet" , "BestFavorite : Inlaz")

            }
        }
    }



    fun checkBottomRecycler(){
        if(scrollBottom){
            (parentFragment as OrderFragment).visibleBtnFilter(true)
        }
        else{
            (parentFragment as OrderFragment).visibleBtnFilter(false)
        }

        if(!scrollDown)
            (parentFragment as OrderFragment).callVisibleNavigationMain(true)
        else
            (parentFragment as OrderFragment).callVisibleNavigationMain(false)
    }

    fun notifidatasetChangeRecycler(){
        adapterListproduct.notifyDataSetChanged()
    }

    fun setScrollToPositionFolowIdCata(id: Int?){
        var p = 0
        val size = arrCatalogy?.size ?: 0
        for(i in 0 until size)
            if(arrCatalogy?.get(i)?.idCatalogy == id)
                p = i

        val y = recylerProduct.getY() + recylerProduct.getChildAt(p).getY()
        nested.smoothScrollTo(0, y.toInt())
    }
}