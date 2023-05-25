package com.example.ontheroad.owner.map

import android.app.ActionBar
import android.app.Dialog
import android.content.Intent
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import com.example.ontheroad.R
import com.example.ontheroad.owner.ownerMain

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ownerMap : Fragment() {
    private lateinit var mMap: GoogleMap
    private lateinit var phone:String
    private lateinit var vehical:String
    private lateinit var dialog:Dialog

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        dialog = Dialog(requireContext())
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.enable_gps)
        val window: Window? =dialog.window
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        phone = arguments?.getString("phone").toString()
        vehical = arguments?.getString("vehicle").toString()
        if(vehical == "P5180") {


            val db = FirebaseDatabase.getInstance()
            val ref = db.getReference("Live_Location")

            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                       // val latitude = snapshot.child("Latitude").getValue() as Double
                       // val longitude = snapshot.child("Longitude").getValue() as Double
                        val latitude = 32.281700
                        val longitude = 72.293137
                        val vehicleLocation = LatLng(latitude, longitude)
                        mMap.clear()
                        mMap.addMarker(
                            MarkerOptions().position(vehicleLocation).title("Vehicle Location")
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vehicleLocation, 16f))


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }else {
            dialog.show()
            val doneBtn = dialog.findViewById<Button>(R.id.enableGps)
            doneBtn.setOnClickListener {
                val intent = Intent(requireContext(), ownerMain::class.java)
                intent.putExtra("phone", phone)
                intent.putExtra("vehical", vehical)
                startActivity(intent)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_owner_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}