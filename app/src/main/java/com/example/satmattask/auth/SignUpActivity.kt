package com.example.satmattask.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.satmattask.databinding.ActivitySignUpBinding
import com.example.satmattask.databinding.DialogEmailVerifyBinding
import com.example.satmattask.model.Users
import com.example.satmattask.utils.Utils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Locale

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var userImageURI: Uri? = null
    private val selectedImage = registerForActivityResult(ActivityResultContracts.GetContent())
    {
        userImageURI = it
        binding.signUpProfileIV.setImageURI(userImageURI)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.apply {
            signUpProfileIV.setOnClickListener {
                selectedImage.launch("image/*")
            }

            signUpAddressTV.setOnClickListener {
                checkLocationPermissions()
            }

            signUpRegisterBTN.setOnClickListener {
                registerUser()
            }

            signUpLoginTV.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                finish()
            }

        }


    }

    private fun registerUser() {
        binding.apply {
            val name = signUpNameET.text.toString()
            val email = signUpEmailET.text.toString()
            val password = signUpPasswordET.text.toString()
            val mobile = signUpMobileET.text.toString()
            val address = signUpAddressTV.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && mobile.isNotEmpty() && address.isNotEmpty()) {
               if (userImageURI == null) {
                    Utils.showToast(this@SignUpActivity, "Select Profile Image")
                    return
                } else if (mobile.length!=10) {
                    Utils.showToast(this@SignUpActivity, "Invalid Phone")
                    return
                } else {
                    saveUser(email, password)
                }
            } else {
                Utils.showToast(this@SignUpActivity, "All fields are mandatory")
                return
            }

        }
    }

    private fun saveUser(
        email: String,
        password: String
    ) {
        Utils.showDialog(this@SignUpActivity)
        lifecycleScope.launch {
            try {
                val firebaseAuth =
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .await()

                if (firebaseAuth != null) {
                    Log.d("SignUpActivity", "User Created Successfully")
                    val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
                    saveProfileImage(currentUserID, userImageURI)
                } else {
                    Utils.hideDialog()
                    Toast.makeText(this@SignUpActivity, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Utils.hideDialog()
                Toast.makeText(this@SignUpActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun saveProfileImage(currentUserID: String?, userImageURI: Uri?) {
        val storageReference =
            FirebaseStorage.getInstance().getReference("Profile").child(currentUserID!!)
                .child("Profile.jpg")
        lifecycleScope.launch {
            try {
                val uploadTask = storageReference.putFile(userImageURI!!).await()
                if (uploadTask.task.isSuccessful) {
                    Log.d("SignUpActivity", "Profile Image Saved Successfully")
                    val imageURL = storageReference.downloadUrl.await()
                    saveUserData(currentUserID, imageURL)
                } else {
                    Utils.hideDialog()
                    Toast.makeText(this@SignUpActivity, "Image upload failed", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Utils.hideDialog()
                Toast.makeText(this@SignUpActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun saveUserData(currentUserID: String, imageURL: Uri) {
        val name = binding.signUpNameET.text.toString()
        val email = binding.signUpEmailET.text.toString()
        val mobile = binding.signUpMobileET.text.toString()
        val address = binding.signUpAddressTV.text.toString()

        lifecycleScope.launch {
            val db = FirebaseDatabase.getInstance().getReference("Users")

            val user = Users(currentUserID, name, email, mobile, address, imageURL.toString())

            try {
                Log.d("SignUpActivity", "User Data Saved Successfully")
                db.child(currentUserID).setValue(user).await()
                Utils.hideDialog()

                sendVerificationMail()
            } catch (e: Exception) {
                Toast.makeText(this@SignUpActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun sendVerificationMail() {
        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
            ?.addOnSuccessListener {
                var dialog =
                    DialogEmailVerifyBinding.inflate(LayoutInflater.from(this@SignUpActivity))
                var alertDialog = AlertDialog.Builder(this@SignUpActivity)
                    .setView(dialog.root)
                    .create()
                alertDialog.show()
                alertDialog.setCancelable(false)
                dialog.emailVerifyOkBTN.setOnClickListener {
                    alertDialog.dismiss()
                    startActivity(
                        Intent(
                            this@SignUpActivity,
                            SignInActivity::class.java
                        )
                    )
                    finish()
                }
            }
    }

    private fun checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //  checkGPS()
            checkGPSUpdated()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    // Handle location updates here
                    // For example, display latitude and longitude
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Toast.makeText(
                        this@SignUpActivity,
                        "Latitude: $latitude, Longitude: $longitude",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // checkGPS()
                    checkGPSUpdated()
                } else {
                    Toast.makeText(this, "Location permission needed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun checkGPS() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 2000

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(
            this.applicationContext
        ).checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                getUserLocation()
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolveApixception = e as ResolvableApiException
                        resolveApixception.startResolutionForResult(this, 200)
                    } catch (intentExc: IntentSender.SendIntentException) {

                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                    }
                }
            }
        }

    }

    private fun checkGPSUpdated() {
        locationHandle()
        var builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        var result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(
            this
        ).checkLocationSettings(builder.build())

        result.addOnCompleteListener(
            object : OnCompleteListener<LocationSettingsResponse?> {
                @SuppressLint("MissingPermission")
                override fun onComplete(task: Task<LocationSettingsResponse?>) {
                    try {
                        val response = task.getResult(ApiException::class.java)
                        val locationCallback: LocationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                if (locationResult != null) {
                                    for (location in locationResult.locations) {
                                        val latitude = location.latitude
                                        val longitude = location.longitude

                                        val geoCoder =
                                            Geocoder(this@SignUpActivity, Locale.getDefault())

                                        val address = geoCoder.getFromLocation(
                                            latitude,
                                            longitude,
                                            1
                                        )

                                        val address_line = address?.get(0)?.getAddressLine(0)

                                        binding.signUpAddressTV.text = address_line
                                    }
                                } else {
                                    Log.e("TurnOnGps", "LocationCallbackCatch")
                                }
                            }
                        }
                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    } catch (e: ApiException) {
                        when (e.statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                val resolvableApiException = e as ResolvableApiException
                                resolvableApiException.startResolutionForResult(
                                    (this@SignUpActivity as Activity?)!!,
                                    200
                                )
                            } catch (ex: SendIntentException) {
                                ex.printStackTrace()
                            }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                        }
                    }
                }
            })
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            val location = task.getResult()

            if (location != null) {
                try {
                    val geoCoder = Geocoder(this, Locale.getDefault())

                    val address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)

                    val address_line = address?.get(0)?.getAddressLine(0)

                    binding.signUpAddressTV.text = address_line

                } catch (e: IOException) {

                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            when (resultCode) {
                Activity.RESULT_OK -> {
//                    checkGPS()
                    checkGPSUpdated()
                }

                Activity.RESULT_CANCELED -> {
                    Utils.showToast(this@SignUpActivity, "Please Enable GPS for Address")
                }
            }
        }
    }

    private fun locationHandle() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this@SignUpActivity)
        locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(5000)
        locationRequest.setFastestInterval((50 / 2).toLong())
    }


}