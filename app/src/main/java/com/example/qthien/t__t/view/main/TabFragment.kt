package com.example.qthien.t__t.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qthien.t__t.adapter.ListProductAdapter
import com.example.qthien.t__t.model.CatalogyProduct
import com.example.qthien.t__t.model.LatLng
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.model.ProductByCatalogy
import com.example.qthien.t__t.presenter.pre_frag_order.PreFragOrder
import kotlinx.android.synthetic.main.fragment_tab.*



class TabFragment : Fragment() , IViewFragOrder {

    override fun successGetLocationAddress(address: String, latLng: LatLng) {

    }
    override fun resultGetAllCatalogy(arrCatalogy: ArrayList<CatalogyProduct>?) {

    }

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
            adapterListproduct.notifyDataSetChanged()
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
//        val listenerScroll = object : EndlessRecyclerViewScrollListener(layoutManager){
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
//                when(type){
//                    1 , 2->{ // Food and Drink
//                        progressLoadMore.visibility = View.VISIBLE
//                        val size = arrCatalogy?.size ?: page + 1
//                        if(page <= size){
//                            catalogyLast = arrCatalogy?.get(page - 1)?.idCatalogy
//                            preFragOrder.getProductsByCatalogy(catalogyLast)
//                        }
//                    }
//                }
//            }
//        }
//        recylerProduct.addOnScrollListener(listenerScroll)
        type = arguments?.getInt("type")
        arrCatalogy = arrayListOf()


        when(type){
            1 , 2 ->{ // Thức ăn or Nước uống
                arrCatalogy = arguments?.getParcelableArrayList("arrCatalogy")
                Log.d("arrrr" , arrCatalogy.toString())
                preFragOrder.getProductsByCatalogy(arrCatalogy?.get(index)?.idCatalogy)
            }
            3 -> { // Đặt nhiều
                preFragOrder.getProductsBestBuy()
            }
            4 -> { // Yêu thích nhiều
                preFragOrder.getProductBestFavorite()
                Log.d("resultGet" , "BestFavorite : Inlaz")

            }
        }
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