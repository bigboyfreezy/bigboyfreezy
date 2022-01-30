package com.farah.notification

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.farah.notification.databinding.ActivityMapsBinding
import com.google.android.gms.location.*

import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.widget.Autocomplete
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


   private lateinit var mMap : GoogleMap
   private lateinit var binding : ActivityMapsBinding
   var database: FirebaseDatabase = FirebaseDatabase.getInstance()
   var ref: DatabaseReference = database.getReference("test")

   override fun onCreate(savedInstanceState : Bundle?) {
      super.onCreate(savedInstanceState)

      binding = ActivityMapsBinding.inflate(layoutInflater)
      setContentView(binding.root)

      // Obtain the SupportMapFragment and get notified when the map is ready to be used.
      val mapFragment = supportFragmentManager
         .findFragmentById(R.id.map) as SupportMapFragment
      mapFragment.getMapAsync(this)
      setupLocClient()
   }

   private lateinit var fusedLocClient: FusedLocationProviderClient
   // use it to request location updates and get the latest location

   override fun onMapReady(googleMap: GoogleMap) {
      mMap = googleMap //initialise map
      getCurrentLocation()
   }
   private fun setupLocClient() {
      fusedLocClient =
         LocationServices.getFusedLocationProviderClient(this)
   }

   // prompt the user to grant/deny access
   private fun requestLocPermissions() {
      ActivityCompat.requestPermissions(this,
         arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), //permission in the manifest
         REQUEST_LOCATION)
   }

   companion object {
      private const val REQUEST_LOCATION = 1 //request code to identify specific permission request
      private const val TAG = "MapsActivity" // for debugging
   }

   private fun getCurrentLocation() {
      // Check if the ACCESS_FINE_LOCATION permission was granted before requesting a location
      if (ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) !=
         PackageManager.PERMISSION_GRANTED) {

         // call requestLocPermissions() if permission isn't granted
         requestLocPermissions()
      } else {



         fusedLocClient.lastLocation.addOnCompleteListener {
            // lastLocation is a task running in the background
            val location = it.result //obtain location
            val User = user()

            //reference to the database

            if (location != null) {


               val latLng = LatLng(location.latitude, location.longitude)
               // create a marker at the exact location
               mMap.addMarker(MarkerOptions().position(latLng)
                  .title("You are currently here!"))
               // create an object that will specify how the camera will be updated
               val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)

               mMap.moveCamera(update)
               //Save the location data to the database
              ref.setValue(location,User)
            } else {
               // if location is null , log an error message
               Log.e(TAG, "No location found")
            }



         }
      }
   }


   override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<String>,
      grantResults: IntArray) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      //check if the request code matches the REQUEST_LOCATION
      if (requestCode == REQUEST_LOCATION)
      {
         //check if grantResults contains PERMISSION_GRANTED.If it does, call getCurrentLocation()
         if (grantResults.size == 1 && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
         } else {
            //if it doesn`t log an error message
            Log.e(TAG, "Location permission has been denied")
         }
      }
   }

}
   // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
   // and once again when the user makes a selection (for example when calling fetchPlace()).



