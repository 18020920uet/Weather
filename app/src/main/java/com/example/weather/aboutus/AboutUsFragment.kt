package com.example.weather.aboutus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {
    private lateinit var binding: FragmentAboutUsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_us, container, false)

        binding.missionButton.setOnClickListener {
            if (binding.missionContent.visibility == View.GONE) {
                binding.missionContent.visibility = View.VISIBLE
            } else {
                binding.missionContent.visibility = View.GONE
            }
        }

        binding.visionButton.setOnClickListener {
            if (binding.visionContent.visibility == View.GONE) {
                binding.visionContent.visibility = View.VISIBLE
            } else {
                binding.visionContent.visibility = View.GONE
            }
        }

        binding.showAppImageButton.setOnClickListener {
            if (binding.sampleImages.visibility == View.GONE) {
                binding.sampleImages.visibility = View.VISIBLE
            } else {
                binding.sampleImages.visibility = View.GONE
            }
        }

        binding.appIntroduceTextButton.setOnClickListener {
            if (binding.appIntroduceContent.visibility == View.GONE) {
                binding.appIntroduceContent.visibility = View.VISIBLE
            } else {
                binding.appIntroduceContent.visibility = View.GONE
            }
        }

        binding.functionIntroduceButton.setOnClickListener {
            if (binding.functionIntroduceContent.visibility == View.GONE) {
                binding.functionIntroduceContent.visibility = View.VISIBLE
            } else {
                binding.functionIntroduceContent.visibility = View.GONE
            }
        }

        binding.developmentIntroduceButton.setOnClickListener {
            if (binding.developmentInformationContent.visibility == View.GONE) {
                binding.developmentInformationContent.visibility = View.VISIBLE
            } else {
                binding.developmentInformationContent.visibility = View.GONE
            }
        }

        return binding.root
    }
}