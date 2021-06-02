package com.example.weather.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.weather.R
import com.example.weather.database.WeatherAppDatabase
import com.example.weather.databinding.FragmentDetailBinding
import com.example.weather.home.DetailViewModelFactory
import com.example.weather.setting.PressureUnit
import com.example.weather.setting.Settings
import com.example.weather.setting.SpeedUnit
import com.example.weather.setting.TemperatureUnit
import com.google.android.material.snackbar.Snackbar

private const val LONGITUDE = "longitude"
private const val LATITUDE = "latitude"
private const val LOCATION_NAME = "locationName"
private const val COUNTRY_NAME = "countryName"

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var locationName: String = ""
    private var countryName: String = ""
    private var isLocationWatched: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getFloat(LATITUDE).toDouble()
            longitude = it.getFloat(LONGITUDE).toDouble()
            locationName = it.getString(LOCATION_NAME).toString()
            countryName = it.getString(COUNTRY_NAME).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = WeatherAppDatabase.getInstance(application).locationDatabaseDAO

        val viewModelFactory = DetailViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        viewModel.settings = loadSettings()

        viewModel.getWatchStatus(latitude, longitude)
        viewModel.getLocationWeatherInformation(latitude, longitude, locationName)

        viewModel.location.observe(viewLifecycleOwner, {
            isLocationWatched = it != null
            activity?.invalidateOptionsMenu()
        })


        val hourlyAdapter = HourlyWeatherInformationAdapter()
        hourlyAdapter.temperatureUnit = viewModel.settings.temperatureUnit
        binding.listOfHourlyWeatherInformation.adapter = hourlyAdapter
        viewModel.listOfHourlyWeatherInformation.observe(viewLifecycleOwner, {
            it?.let {
                hourlyAdapter.submitList(it)
            }
        })

        viewModel.notification.observe(viewLifecycleOwner, { message ->
            message?.let {
                if (message != "") {
                    Snackbar.make(getViewContent(), message, Snackbar.LENGTH_SHORT).show()
                    viewModel.onShowNotificationComplete()
                }
            }
        })

        val dailyAdapter = DailyWeatherInformationAdapter()
        dailyAdapter.temperatureUnit = viewModel.settings.temperatureUnit
        binding.listOfDailyWeatherInformation.adapter = dailyAdapter
        viewModel.listOfDailyWeatherInformation.observe(viewLifecycleOwner, {
            it?.let {
                dailyAdapter.submitList(it)
            }
        })

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
    }


    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
        if (shareIntent().resolveActivity(requireActivity().packageManager) == null) {
            menu.findItem(R.id.share).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> share()
            R.id.watch_intent -> binding.viewModel!!.handleWatchIntent()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.watch_intent)
        if (isLocationWatched) {
            item.title = "Unwatch"
        } else {
            item.title = "Watch"
        }
        super.onPrepareOptionsMenu(menu)
    }

    private fun shareIntent(): Intent {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        val shareText = binding.viewModel!!.getShareText()
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        return shareIntent
    }

    private fun share() = startActivity(shareIntent())

    private fun getViewContent(): View = requireActivity().findViewById(android.R.id.content)

    private fun loadSettings(): Settings {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val temperatureUnitString = sp.getString("temperatureUnit", "Celsius")
        val pressureUnitString = sp.getString("pressureUnit", "Pa")
        val speedUnitString = sp.getString("speedUnit", "metresPerSecond")

        val temperatureUnit: TemperatureUnit = when (temperatureUnitString) {
            "Kelvin" -> TemperatureUnit.Kelvin
            "Fahrenheit" -> TemperatureUnit.Fahrenheit
            else -> TemperatureUnit.Celsius
        }

        val pressureUnit: PressureUnit = when (pressureUnitString) {
            "hPa" -> PressureUnit.hPa
            else -> PressureUnit.Pa
        }

        val speedUnit: SpeedUnit = when (speedUnitString) {
            "kilometersPerSHour" -> SpeedUnit.kilometersPerSHour
            "milesPerHour" -> SpeedUnit.milesPerHour
            else -> SpeedUnit.metresPerSecond
        }

        return Settings(temperatureUnit, speedUnit, pressureUnit)
    }
}