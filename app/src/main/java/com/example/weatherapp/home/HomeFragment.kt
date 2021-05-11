package com.example.weatherapp.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.Settings
import com.example.weatherapp.database.WeatherAppDatabase
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var permissionId = 1010
    private var aflPermission = Manifest.permission.ACCESS_FINE_LOCATION
    private var aclPermission = Manifest.permission.ACCESS_COARSE_LOCATION
    private var permissions = arrayOf(aflPermission, aclPermission)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = WeatherAppDatabase.getInstance(application).locationDatabaseDAO

        val viewModelFactory = HomeViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        viewModel.settings = Settings()

        binding.viewModel = viewModel

//        viewModel.setCurrentLocationEvent.observe(viewLifecycleOwner, { status ->
//            status?.let {
//                if (ActivityCompat.checkSelfPermission(context!!, aflPermission) != 0
//                    && ActivityCompat.checkSelfPermission(context!!, aclPermission) != 0
//                ) {
//                    requirePermissions()
//                }
//            }
//        })

        viewModel.notification.observe(viewLifecycleOwner, { message ->
            message?.let {
                if (message != "") {
                    if (message == "Require position") {
                        Snackbar.make(getViewContent(), message, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Set", { preSetPosition() })
                            .show()
                    }
                } else {
                    Snackbar.make(getViewContent(), message, Snackbar.LENGTH_SHORT).show()
                    viewModel.onShowNotificationComplete()
                }

            }
        })

        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun getViewContent(): View {
        return activity!!.findViewById(android.R.id.content)
    }

    private fun requirePermissions() {
        requestPermissions(permissions, permissionId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.find_location_option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.navigate_to_find_location_fragment) {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFindLocationFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setPosition() {
        try {
            if (ActivityCompat.checkSelfPermission(context!!, aflPermission)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context!!, aclPermission)
                == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

                val locationRequest = LocationRequest.create().apply {
                    interval = 10000
                    fastestInterval = 5000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }

                val locationCallback = object : LocationCallback() {}

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )

                fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
                    location?.let {
                        binding.viewModel?.saveLocation(location.latitude, location.longitude)
                        fusedLocationClient.removeLocationUpdates(locationCallback)
                    }
                }
            }
        } catch (e: Exception) {
            e.message?.let { binding.viewModel!!.setNotification(it) }
        }
    }

    private fun preSetPosition() {
        requirePermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        Timber.i("onRequestPermissionResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionId && grantResults.size > 1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                binding.viewModel!!.setNotification("Require position")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setPosition()
            }
        }
    }

    fun loadSettings() {
        // TODO: Implement loadSettings
    }
}
