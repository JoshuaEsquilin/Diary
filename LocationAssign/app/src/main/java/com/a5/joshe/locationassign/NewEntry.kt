package com.a5.joshe.locationassign

import android.Manifest
import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_new_entry.*
import java.io.IOException
import android.location.Address

// Author:       Joshua Esquilin
// Date:         4/25/2018
// Description:  This is the NewEntry class that handles making a new diary entry

class NewEntry : AppCompatActivity(), LocationListener, OnMapReadyCallback  {

    var mMap: GoogleMap? = null
    var currentLocMarker: Marker? = null
    var decision_id:Int = 0
    var latitude_holder:String = ""
    var longitude_holder:String = ""
    var location_holder:String = ""

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)

        // Display the map at the bottom of the form
        val mapFragment = MapFragment.newInstance()
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.map_space, mapFragment)
        ft.commit()
        mapFragment.getMapAsync(this)

        // Checks to see what the current purpose of the activity is.  Adding or Updating
        decision_id = intent.getIntExtra("dId", 0)

        // This is the Add Case
        if (decision_id == 0) {

            button_add.text = "Add Entry"

        } else { // This is an Update case

            button_add.text = "Update Entry"
            var _dDate = intent.getStringExtra("dDate")
            var _dSubject = intent.getStringExtra("dSubject")
            var _dEntry = intent.getStringExtra("dEntry")
            var _dLat = intent.getStringExtra("dLat")
            var _dLon = intent.getStringExtra("dLon")
            var _dSource = intent.getStringExtra("dSource")

            editDate.setText(_dDate)
            editSubject.setText(_dSubject)
            editEntry.setText(_dEntry)
            editLat.setText(_dLat)
            editLon.setText(_dLon)
            editLoc.setText(_dSource)

        }

        // Pressing the add entry button saves what was entered and the location information
        // It also checks to see if the user entered in all the fields
        button_add.setOnClickListener {

            var dateT = editDate.text.toString()
            var subT = editSubject.text.toString()
            var entT = editEntry.text.toString()
            var latT = editLat.text.toString()
            var lonT = editLon.text.toString()
            var sourceT = editLoc.text.toString()

            if(dateT == ""){

                Toast.makeText(this, "Enter a Date",
                        Toast.LENGTH_SHORT).show()

            }else if (subT == ""){

            Toast.makeText(this, "Enter a Subject",
                    Toast.LENGTH_SHORT).show()

            }else if (entT == ""){

                Toast.makeText(this, "Enter an Entry",
                        Toast.LENGTH_SHORT).show()

            }else if (latT == ""){

                Toast.makeText(this, "Please wait for a latitude to be found",
                        Toast.LENGTH_SHORT).show()

            }else if (lonT == ""){

                Toast.makeText(this, "Please wait for a longitude to be found",
                        Toast.LENGTH_SHORT).show()

            }else if (sourceT == ""){

                Toast.makeText(this, "Please wait for a location method to be established",
                        Toast.LENGTH_SHORT).show()
            }
            else{ // Everything is valid, so now save the information

                var values = ContentValues()
                values.put("dDate", dateT)
                values.put("dSubject", subT)
                values.put("dEntry", entT)
                values.put("dLat", latT)
                values.put("dLon", lonT)
                values.put("dSource", sourceT)

                // In the adding case
                if(decision_id == 0){

                    var dbHand = DbHandler(this)
                    var save = dbHand.saveData(values)

                    if(save == "ok") {

                        Toast.makeText(this, "Entry is added", Toast.LENGTH_SHORT).show()

                        var intent = Intent(this, AllEntries::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "Entry was not added", Toast.LENGTH_SHORT).show()
                    }

                }else{ // In the updating case

                    var dbHand = DbHandler(this)
                    var up: String = dbHand.updateData(values, decision_id)

                    if(up == "ok") {
                        Toast.makeText(this, "Entry is updated", Toast.LENGTH_SHORT).show()

                        var intent = Intent(this, AllEntries::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "Entry was not updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // The deletion button handles deleting the entry from the database
        button_delete.setOnClickListener{

            var dbHand = DbHandler(this)
            var delete: String = dbHand.deleteData(decision_id)

            if(delete =="ok") {

                Toast.makeText(this, "Entry Deleted", Toast.LENGTH_SHORT).show()
            }else{

                Toast.makeText(this, "Entry was not deleted", Toast.LENGTH_SHORT).show()
            }

            var intent = Intent(this, AllEntries::class.java)
            startActivity(intent)
            finish()
        }

        // Permissions for locations
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val permis = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(permis, 1)
        } else {
            subscribeLocationProvider()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        subscribeLocationProvider()
    }

    override fun onLocationChanged(location: Location) {

        // In the add record case, the latitude and longitude is constantly being updated, until
        // the user decides to save it.
        if (decision_id == 0) {

            editLat.text = location.latitude.toString()
            editLon.text = location.longitude.toString()
            editLoc.text = location.provider
            latitude_holder = editLat.text.toString()
            longitude_holder = editLon.text.toString()
            location_holder = editLoc.text.toString()

            val local = LatLng(latitude_holder.toDouble(), longitude_holder.toDouble())
            currentLocMarker = mMap?.addMarker(MarkerOptions().position(local).title(getAddress(local)))
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(local))
        }

        // Update Case
        latitude_holder = editLat.text.toString()
        longitude_holder = editLon.text.toString()
        location_holder = editLoc.text.toString()
    }

    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {
    }

    override fun onProviderEnabled(s: String) {
    }

    override fun onProviderDisabled(s: String) {
    }

    public override fun onStart() {
        super.onStart()
        subscribeLocationProvider()
    }

    @TargetApi(Build.VERSION_CODES.M)
    public override fun onStop() {
        super.onStop()

        val locman = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locman.removeUpdates(this)
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locman.removeUpdates(this)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun subscribeLocationProvider() {

        val locman = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locman.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap

        // Defaults to Sydney if there is no current location, but if there is a saved longitude and
        // latitude, then it goes to what's saved.
        if (intent.getStringExtra("dLat") != null && intent.getStringExtra("dLon") != null) {

            val local = LatLng(intent.getStringExtra("dLat").toDouble(), intent.getStringExtra("dLon").toDouble())
            currentLocMarker = mMap?.addMarker(MarkerOptions().position(local).title(getAddress(local)))
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(local))


        } else {
            val sydney = LatLng(-33.867, 151.206)
            currentLocMarker = mMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }

    }

    // This was supposed to grab the address of the saved location, but it kept coming up blank
    private fun getAddress(lalo: LatLng): String {

        val geo = Geocoder(this)
        val addressList: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            addressList = geo.getFromLocation(lalo.latitude, lalo.longitude, 1)
            if (null != addressList && !addressList.isEmpty()) {
                address = addressList[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText +=
                            if (i == 0){
                                address.getAddressLine(i)
                            } else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e("NewEntry", e.localizedMessage)
        }
        return addressText
    }
}
