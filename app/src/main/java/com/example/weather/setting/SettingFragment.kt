package com.example.weather.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.weather.R

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}