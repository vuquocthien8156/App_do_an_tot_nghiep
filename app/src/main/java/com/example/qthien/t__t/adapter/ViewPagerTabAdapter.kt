package com.example.qthien.t__t.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import com.example.qthien.t__t.model.CatalogyProduct
import com.example.qthien.t__t.mvp.view.main.TabFragment





class ViewPagerTabAdapter(
    fm: FragmentManager,
    internal var arrTitle: ArrayList<String>,
    var arrCatalogyProduct: ArrayList<CatalogyProduct>?
) : FragmentPagerAdapter(fm) {

    private var registeredFragments = SparseArray<Fragment>()

    override fun getItem(position: Int): Fragment? {
        val fragment : Fragment
        Log.d("Positionnnn" , position.toString())

        fragment = when(position){
            0 -> TabFragment.newInstance(3)
            1 -> TabFragment.newInstance(4)
            2 -> {
                val arr = arrCatalogyProduct?.filter { it.mainCatelogy == 2 }?.toCollection(ArrayList())
                Log.d("Arr1" , arr.toString())
                TabFragment.newInstance(2, arr)
            }
            else -> {
                val arr = arrCatalogyProduct?.filter { it.mainCatelogy == 1 }?.toCollection(ArrayList())
                Log.d("Arr2" , arr.toString())
                TabFragment.newInstance(1 , arr)
            }
        }
        return fragment
    }

    override fun getItemPosition(`object`: Any): Int {
        val f = `object` as TabFragment?
        if(f != null)
            f.notifidatasetChangeRecycler()
        Log.d("chanesss" , "true")
        return super.getItemPosition(`object`);
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment {
        return registeredFragments.get(position)
    }

    override fun getCount(): Int {
        return arrTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return arrTitle[position]
    }
}
