package com.example.qthien.t__t.view.search_product

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.SearchProductAdapter
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.presenter.pre_search_product.PreSearchProductActivity
import kotlinx.android.synthetic.main.activity_search_product.*
import java.text.Normalizer
import java.util.regex.Pattern

class SearchProductActivity : AppCompatActivity() , IViewSearchProductActivity {

    private val ALPHABETICAL_COMPARATOR = object : Comparator<Product> {
        override fun compare(a: Product, b: Product): Int {
            return a.nameProduct.compareTo(b.nameProduct)
        }
    }

    lateinit var adapter : SearchProductAdapter
    lateinit var arrProducts : ArrayList<Product>
    lateinit var preSearchProductActivity: PreSearchProductActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)

        setSupportActionBar(toolbarSearch)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        preSearchProductActivity = PreSearchProductActivity(this)

        arrProducts = ArrayList()
        adapter = SearchProductAdapter(this , ALPHABETICAL_COMPARATOR)
        recylerSearch.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recylerSearch.adapter = adapter

        preSearchProductActivity.getAllProduct()
    }

    private fun eventEdtSearch() {
        edtSearchProduct.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
                val lowerCaseQuery = query.toString().toLowerCase()
                val filteredModelList = arrProducts.filter {
                    it.nameProduct.toLowerCase().contains(lowerCaseQuery)
                    || it.decriptionProduct.toLowerCase().contains(lowerCaseQuery)
                    || deAccent(it.nameProduct).toLowerCase().contains(lowerCaseQuery)}
                adapter.replaceAll(filteredModelList)
                recylerSearch.scrollToPosition(0)
            }
        })
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun resultGetAllProduct(arrResult: ArrayList<Product>?) {
        if(arrResult != null) {
            arrProducts.addAll(arrResult)
            eventEdtSearch()
        }
        adapter.add(arrProducts)
    }

    override fun failure(message: String) {
        toast(message)
    }

    fun toast(message : String){
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }
}
