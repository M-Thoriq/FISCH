package com.harissabil.fisch.ui.main

import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.harissabil.fisch.databinding.ActivityMainBinding
import com.harissabil.fisch.ui.ViewModelFactory
import com.harissabil.fisch.ui.add.AddActivity
import com.harissabil.fisch.ui.history.HistoryActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        createLocationRequest()
        createLocationCallback()
        startLocationUpdates()

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        binding.rvToday.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupViewModel() {
//        mainViewModel.getWeather(52.52, 13.41, true)
        mainViewModel.weatherResponse.observe(this) { weather ->
            binding.apply {
                tvWeather.text = "${weather.currentWeather.temperature}°C"
                tvWind.text = "${weather.currentWeather.windspeed}Km/h"
            }
        }
        mainViewModel.getPrice().observe(this) { price ->
            if (price.isNotEmpty()) {
                binding.tvTotal.text = "Rp. ${price.sum()}"
            } else {
                binding.tvTotal.text = "Rp. 0"
            }
        }
        mainViewModel.getAllFish().observe(this) { fish ->
            if (fish.isNotEmpty()) {
                binding.tvTotalIkan.text = "Total ikan: ${fish.size}"
            } else {
                binding.tvTotalIkan.text = "Total ikan: 0"
            }
        }
        mainViewModel.getWeight().observe(this) { weight ->
            if (weight.isNotEmpty()) {
                binding.tvTotalBerat.text = "Total berat: ${weight.sum()} kg"
            } else {
                binding.tvTotalBerat.text = "Total berat: 0 kg"
            }
        }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)
        mainViewModel.getFishByDate(current).observe(this) { fish ->
            if (fish.isNotEmpty()) {
                binding.tvNoToday.visibility = View.GONE
                binding.rvToday.adapter = TodayFishListAdapter(fish)
            } else {
                binding.tvNoToday.visibility = View.VISIBLE
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            ivHistory.setOnClickListener {
                startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
            }
            fabAdd.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddActivity::class.java))
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }

                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }

                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) && checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    mainViewModel.getWeather(location.latitude, location.longitude, true)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK ->
                    Log.i(TAG, "onActivityResult: All location settings are satisfied.")

                RESULT_CANCELED ->
                    Toast.makeText(
                        this@MainActivity,
                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getMyLastLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this@MainActivity, sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d(TAG, "onLocationResult: " + location.latitude + ", " + location.longitude)
                    mainViewModel.getWeather(location.latitude, location.longitude, true)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            Log.e(TAG, "Error : " + exception.message)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}