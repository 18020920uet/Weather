package com.example.weather.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.database.WeatherAppDatabase
import com.example.weather.databinding.FragmentDetailBinding
import com.example.weather.home.DetailViewModelFactory

private const val LONGITUDE = "longitude"
private const val LATITUDE = "latitude"
private const val LOCATION_NAME = "locationName"
private const val ID = "id"

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var locationName: String = ""
    private var locationId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getFloat(LATITUDE).toDouble()
            longitude = it.getFloat(LONGITUDE).toDouble()
            locationName = it.getString(LOCATION_NAME).toString()
            locationId = it.getInt(ID)
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

        viewModel.getLocationWeatherInformation(latitude, longitude, locationName)
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        val hourlyAdapter = HourlyWeatherInformationAdapter()
        hourlyAdapter.temperatureUnit = viewModel.settings.temperatureUnit
        binding.listOfHourlyWeatherInformation.adapter = hourlyAdapter
        viewModel.listOfHourlyWeatherInformation.observe(viewLifecycleOwner, {
            it?.let {
                hourlyAdapter.submitList(it)
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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.watch_intent)
        if (item.title == "Watch") {
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

    private fun share() {
        startActivity(shareIntent())
    }
}