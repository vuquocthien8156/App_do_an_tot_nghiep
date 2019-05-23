package com.example.qthien.t__t.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.qthien.t__t.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val location = LatLng(10.853520, 106.731070)
        mMap.addMarker(MarkerOptions().position(location).title(getString(R.string.my_location)))

        val cameraPosition = CameraPosition.Builder()
            .target(
                location
            )      // Sets the center of the map to location user
            .zoom(17f)                   // Sets the zoom
            .bearing(90f)                // Sets the orientation of the camera to east
            .tilt(30f)                   // Sets the tilt of the camera to 30 degrees
            .build()                   // Creates a CameraPosition from the builder

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap.uiSettings.isMyLocationButtonEnabled = true
    }
}
