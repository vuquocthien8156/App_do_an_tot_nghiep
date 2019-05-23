package com.example.qthien.t__t.adapter

import android.content.Context
import android.graphics.Typeface
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
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.android.synthetic.main.item_recy_search_place.view.*
import java.util.concurrent.TimeUnit


class PlaceAutocompleteAdapter(
    internal var mContext: Context, private val layout: Int, private val mGoogleApiClient: GoogleApiClient,
    private var mBounds: LatLngBounds?, private val mPlaceFilter: AutocompleteFilter? = null
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


    /**
     * Sets the bounds for all subsequent queries.
     */
    fun setBounds(bounds: LatLngBounds) {
        mBounds = bounds
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint)
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList
                        results.count = mResultList!!.size
                    }
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    Log.i("mGoogleApiClient", "Filter complete")
                    notifyDataSetChanged()
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                    Log.i("mGoogleApiClient", "Filter fail")
                }
            }
        }
    }

    private fun getAutocomplete(constraint: CharSequence?): ArrayList<PlaceAutocomplete>? {
        if (mGoogleApiClient.isConnected) {
            Log.i("mGoogleApiClient", "Starting autocomplete query for: " + constraint!!)

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            val results = Places.GeoDataApi
                .getAutocompletePredictions(
                    mGoogleApiClient, constraint.toString(),
                    mBounds, mPlaceFilter
                )

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            val autocompletePredictions = results
                .await(60, TimeUnit.SECONDS)

            // Confirm that the query completed successfully, otherwise return null
            val status = autocompletePredictions.status
            if (!status.isSuccess) {
                //                Toast.makeText(mContext, "Error contacting API: " + status.toString(),
                //                        Toast.LENGTH_SHORT).show();
                Log.e("mGoogleApiClient", "Error getting autocomplete prediction API call: $status")
                autocompletePredictions.release()
                return null
            }

            Log.i(
                "mGoogleApiClient", "Query completed. Received " + autocompletePredictions.count
                        + " predictions."
            )

            // Copy the results into our own data structure, because we can't hold onto the buffer.
            // AutocompletePrediction objects encapsulate the API response (place ID and description).

            val iterator = autocompletePredictions.iterator()
            val resultList = ArrayList<PlaceAutocomplete>(autocompletePredictions.count)
            val STYLE_BOLD = StyleSpan(Typeface.BOLD)
            val STYLE_BOLD1 = StyleSpan(Typeface.NORMAL)
            while (iterator.hasNext()) {
                val prediction = iterator.next()
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                resultList.add(
                    PlaceAutocomplete(
                        prediction.placeId.toString(),
                        prediction.getPrimaryText(STYLE_BOLD),
                        prediction.getSecondaryText(STYLE_BOLD1)
                )
                )
            }

            // Release the buffer now that all data has been copied.
            autocompletePredictions.release()

            return resultList
        }
        Log.e("mGoogleApiClient", "Google API client is not connected for autocomplete query.")
        return null
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlaceViewHolder {
        val layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView = layoutInflater.inflate(layout, viewGroup, false)
        return PlaceViewHolder(convertView)
    }


    override fun onBindViewHolder(mPredictionHolder: PlaceViewHolder, i: Int) {
        mPredictionHolder.mAddressSecond.text = mResultList!![i].seccondText
        mPredictionHolder.mAddressPrimary.text = mResultList!![i].primaryText

        mPredictionHolder.mParentLayout.setOnClickListener { mListener.onPlaceClick(mResultList, i) }
    }

    override fun getItemCount(): Int {
        return if (mResultList != null)
            mResultList!!.size
        else
            0
    }

    fun getItem(position: Int): PlaceAutocomplete {
        return mResultList!![position]
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
                                                       var seccondText: CharSequence) {
        override fun toString(): String {
            return seccondText.toString()
        }
    }

    companion object {
        private val TAG = "PlaceAutocompleteAdapter"
    }
}