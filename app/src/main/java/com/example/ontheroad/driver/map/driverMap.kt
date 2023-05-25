package com.example.ontheroad.driver.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.media.audiofx.BassBoost
import android.net.Uri
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import com.example.ontheroad.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class driverMap : Fragment() {
        private lateinit var map:GoogleMap
        private  var REQUEST_LOCATION_PERMISSION = 1

        @SuppressLint("MissingPermission")
        private val callback = OnMapReadyCallback { googleMap ->
            map = googleMap

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),REQUEST_LOCATION_PERMISSION)

                return@OnMapReadyCallback
            }else{
                map.uiSettings.isMyLocationButtonEnabled = true
                map.isMyLocationEnabled = true
                val sharebtn = view?.findViewById<ImageButton>(R.id.directions_button)
                sharebtn?.setOnClickListener {
                    val latLng = map.cameraPosition.target
                    val uri = "http://maps.google.com/maps?q=loc:${latLng.latitude},${latLng.longitude}&z=16"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    intent.setPackage("com.google.android.apps.maps")
                    startActivity(intent)

                }
            }


        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            val view = inflater.inflate(R.layout.fragment_driver_map, container, false)
            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(callback)
        }

        @SuppressLint("MissingPermission")
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            if(requestCode == REQUEST_LOCATION_PERMISSION){
                if (grantResults.isNotEmpty() &&grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    map.uiSettings.isMyLocationButtonEnabled = true
                    map.isMyLocationEnabled = true
                }
            }
        }

    }
