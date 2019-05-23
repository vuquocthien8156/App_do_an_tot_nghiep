package com.example.qthien.t__t.view.product_favorite

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.widget.Toast
import com.example.qthien.t__t.adapter.ProductFavoriteAdapter
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.presenter.pre_product_favorite.PreProductFavoriteActi
import com.example.qthien.t__t.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_product_favorite.*
import java.text.Normalizer
import java.util.regex.Pattern


class ProductFavoriteActivity : AppCompatActivity() , IViewProductFavoriteActi {

    override fun favoriteProduct(resultCode : String) {}

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {
        if(arrResult != null){
            arrProducts.clear()
            arrProducts.addAll(arrResult)
            adapter.clear()
            adapter.add(arrResult)
        }

        swipeRefreshLayout.setRefreshing(false)
    }

    override fun failureFavorite(message: String) {
        swipeRefreshLayout.setRefreshing(false)
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    private val ALPHABETICAL_COMPARATOR = object : Comparator<Product> {
        override fun compare(a: Product, b: Product): Int {
            return a.nameProduct.compareTo(b.nameProduct)
        }
    }

    lateinit var adapter : ProductFavoriteAdapter
    lateinit var arrProducts : ArrayList<Product>
    lateinit var preProductFavorite : PreProductFavoriteActi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_product_favorite)

        setSupportActionBar(toolbarProductFavorite)
        toolbarProductFavorite.setTitleTextColor(Color.WHITE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(com.example.qthien.t__t.R.string.products_like)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)

        preProductFavorite = PreProductFavoriteActi(this)

        arrProducts = ArrayList()
        adapter = ProductFavoriteAdapter(this , ALPHABETICAL_COMPARATOR)
        recyProductsFavorite.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyProductsFavorite.adapter = adapter

        preProductFavorite.getProductFavorite(MainActivity.customer!!.idCustomer)

        swipeRefreshLayout.setOnRefreshListener{
            preProductFavorite.getProductFavorite(MainActivity.customer!!.idCustomer)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_product_favorite, menu)
        val menuItem = menu?.findItem(com.example.qthien.t__t.R.id.menu_search_product_favorite)
        val searchView = menuItem?.actionView as SearchView
        searchView.setBackgroundColor(Color.WHITE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val lowerCaseQuery = query.toString().toLowerCase()
                val filteredModelList = arrProducts.filter { it.nameProduct.toLowerCase().contains(lowerCaseQuery)
                        || deAccent(it.nameProduct).toLowerCase().contains(lowerCaseQuery) }
                adapter.replaceAll(filteredModelList)
                recyProductsFavorite.scrollToPosition(0)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun deAccent(str: String): String {
        val nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(nfdNormalizedString).replaceAll("")
    }

    private fun filter(models: List<Product>, query: String?): List<Product> {
        val lowerCaseQuery = query?.toLowerCase()

        val filteredModelList = ArrayList<Product>()
        if(lowerCaseQuery != null){
            for (model in models) {
                val text = model.nameProduct.toLowerCase()
                if (text.contains(lowerCaseQuery)) {
                    filteredModelList.add(model)
                }
            }
        }

        return filteredModelList
    }
}
