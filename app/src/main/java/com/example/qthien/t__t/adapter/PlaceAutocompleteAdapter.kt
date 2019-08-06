package com.example.qthien.t__t.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v4.app.DialogFragment.STYLE_NORMAL
import android.support.v7.widget.RecyclerView
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.item_recy_search_place.view.*
import com.google.android.libraries.places.api.Places
import kotlinx.android.synthetic.main.item_recy_search_place.view.*
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class PlaceAutocompleteAdapter(
    internal var mContext: Context, private val layout: Int
) : RecyclerView.Adapter<PlaceAutocompleteAdapter.PlaceViewHolder>(), Filterable {

    internal var mListener: PlaceAutoCompleteInterface
    internal var mResultList: ArrayList<PlaceAutocomplete>? = null

    interface PlaceAutoCompleteInterface {
        fun onPlaceClick(mResultList: ArrayList<PlaceAutocomplete>?, position: Int)
    }

    init {
        this.mListener = mContext as PlaceAutoCompleteInterface
    }

    /*
    Clear List items
     */
    fun clearList() {
        if (mResultList != null && mResultList!!.size > 0) {
            mResultList!!.clear()
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                // Skip the autocomplete query if no constraints are given.
                Log.d("mGoogleApiClient" ,"1")
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint)
                    Log.d("mGoogleApiClient" ,"size = " + mResultList?.size.toString())

                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList
                        results.count = mResultList!!.size
                    }
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults?) {
                Log.d("mGoogleApiClient" ,"size2 = " + results?.count.toString())
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    Log.d("mGoogleApiClient", "Filter complete")
                    notifyDataSetChanged()
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                    Log.d("mGoogleApiClient", "Filter fail")
                }
            }
        }
    }

    private fun getAutocomplete(constraint: CharSequence?): ArrayList<PlaceAutocomplete>? {
        // Initialize Places.
        Log.d("mGoogleApiClient" ,"2")

        Places.initialize(mContext , mContext.getString(com.example.qthien.t__t.R.string.google_maps_key) )

        // Create a new Places client instance.
        val placesClient = Places.createClient(mContext)

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()
        Log.d("mGoogleApiClient" ,"2.1")

        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request = FindAutocompletePredictionsRequest.builder()
            // Call either setLocationBias() OR setLocationRestriction().
//            .setLocationBias(bounds)
            //.setLocationRestriction(bounds)
            .setCountry("vn")
            .setTypeFilter(TypeFilter.ADDRESS)
            .setSessionToken(token)
            .setQuery(constraint.toString())
            .build()
        Log.d("mGoogleApiClient" ,"2.2")

        val autocompletePredictions = placesClient.findAutocompletePredictions(request)

        Log.d("mGoogleApiClient" ,"2.3")
        try {
            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            Log.d("mGoogleApiClient" ,"2.3 " + e.message.toString())
        } catch (e: InterruptedException) {
            Log.d("mGoogleApiClient" ,"2.3 " + e.message.toString())
        } catch (e: TimeoutException) {
            Log.d("mGoogleApiClient" ,"2.3 " + e.message.toString())
        }



        Log.d("mGoogleApiClient" ,"2.4")
        val resultList = ArrayList<PlaceAutocomplete>()
        val STYLE_BOLD = StyleSpan(Typeface.BOLD)
        val STYLE_BOLD1 = StyleSpan(Typeface.NORMAL)

         if (autocompletePredictions.isSuccessful()) {
            val findAutocompletePredictionsResponse = autocompletePredictions.result
             Log.d("mGoogleApiClient" , (findAutocompletePredictionsResponse != null).toString())
             if (findAutocompletePredictionsResponse != null) {
                 Log.d("mGoogleApiClient" , "size0 = " + (findAutocompletePredictionsResponse.autocompletePredictions.size).toString())
                 for (prediction in findAutocompletePredictionsResponse.autocompletePredictions) {
                    resultList.add(
                        PlaceAutocomplete(
                            prediction.placeId,
                            prediction.getPrimaryText(STYLE_BOLD),
                            prediction.getSecondaryText(STYLE_BOLD1),
                            prediction.getFullText(STYLE_BOLD)
                        )
                    )
                }
            }
        }
        return resultList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlaceViewHolder {
        val layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = layoutInflater.inflate(layout, viewGroup, false)
        return PlaceViewHolder(convertView)
    }


    override fun onBindViewHolder(mPredictionHolder: PlaceViewHolder, i: Int) {
        mPredictionHolder.mAddressSecond.text = mResultList?.get(i)?.seccondText
        mPredictionHolder.mAddressPrimary.text = mResultList?.get(i)?.primaryText

        mPredictionHolder.mParentLayout.setOnClickListener { mListener.onPlaceClick(mResultList, i) }
    }

    override fun getItemCount(): Int {
        return if (mResultList != null)
            mResultList!!.size
        else
            0
    }

    fun getItem(position: Int): PlaceAutocomplete {
        return mResultList?.get(position)!!
    }

    /*
    View Holder For Trip History
     */
    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //        CardView mCardView;
        var mParentLayout: RelativeLayout
        var mAddressPrimary : TextView
        var mAddressSecond : TextView

        init {
            mParentLayout = itemView.predictedRow
            mAddressPrimary = itemView.addressPrimary
            mAddressSecond = itemView.addressSeccond
        }

    }

    /**
     * Holder for Places Geo Data Autocomplete API results.
     */
    inner class PlaceAutocomplete internal constructor(var placeId: CharSequence,
                                                       var primaryText: CharSequence,
                                                       var seccondText: CharSequence,
                                                       var fullText : CharSequence) {
        override fun toString(): String {
            return seccondText.toString()
        }
    }

    companion object {
        private val TAG = "PlaceAutocompleteAdapter"
    }
}