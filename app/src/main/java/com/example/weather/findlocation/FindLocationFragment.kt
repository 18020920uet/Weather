package com.example.weather.findlocation

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weather.R
import com.example.weather.database.WeatherAppDatabase
import com.example.weather.databinding.FragmentFindLocationBinding
import com.google.android.material.snackbar.Snackbar

class FindLocationFragment : Fragment() {
    lateinit var binding: FragmentFindLocationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_find_location, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = WeatherAppDatabase.getInstance(application).suggestLocationDatabaseDAO

        val viewModelFactory = FindLocationViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(FindLocationViewModel::class.java)

        val suggestLocationAdapter = SuggestLocationAdapter(SuggestLocationListener {
            viewModel.navigateToDetailFragment()
            findNavController().navigate(
                FindLocationFragmentDirections.actionFindLocationFragmentToDetailFragment(
                    it.latitude.toFloat(),
                    it.longitude.toFloat(),
                    it.locationName,
                    it.country
                )
            )
            viewModel.onNavigateCompleted()
        })

        binding.listOfSuggestLocations.adapter = suggestLocationAdapter
        viewModel.listOfSuggestLocations.observe(viewLifecycleOwner, {
            it?.let {
                suggestLocationAdapter.submitList(it)
            }
        })

        val relatedNameLocationAdapter = RelatedNameLocationAdapter(RelatedNameLocationListener {
            viewModel.navigateToDetailFragment()
            findNavController().navigate(
                FindLocationFragmentDirections.actionFindLocationFragmentToDetailFragment(
                    it.latitude.toFloat(),
                    it.longitude.toFloat(),
                    it.name,
                    it.country
                )
            )
            viewModel.onNavigateCompleted()
        })

        binding.listOfRelatedNameLocations.adapter = relatedNameLocationAdapter
        viewModel.listRelatedNameLocations.observe(viewLifecycleOwner, {
            it?.let {
                relatedNameLocationAdapter.submitList(it)
            }
        })

        viewModel.notification.observe(viewLifecycleOwner, { message ->
            message?.let {
                if (it != "") {
                    Snackbar.make(getViewContent(), it, Snackbar.LENGTH_LONG).show()
                    viewModel.onShowNotificationComplete()
                }
            }
        })

        binding.viewModel = viewModel
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.find_location_option_menu, menu)
        val searchView: SearchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = "Search here..."
        searchView.isSubmitButtonEnabled

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.viewModel!!.findLocationByName(it)
                    binding.resultText.visibility = View.VISIBLE
                    val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE)
                    (imm as (InputMethodManager)).hideSoftInputFromWindow(view!!.windowToken, 0)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun getViewContent(): View {
        return requireActivity().findViewById(android.R.id.content)
    }
}