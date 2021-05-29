package com.example.weather.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingBinding

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentSettingBinding = DataBindingUtil.inflate<FragmentSettingBinding>(
            inflater,
            R.layout.fragment_setting,
            container,
            false
        )

        val togg = binding.toggleButtonGroupId
        //val C=binding.CId

        togg.addOnButtonCheckedListener { togg, checkedId, isChecked ->
            if (isChecked) {
               /* if (togg.checkedButtonId == R.id.CId) {
                    showToast("C")
                } else if (togg.checkedButtonId == R.id.FId) {
                    showToast("F")
                } else {
                    showToast("K")
                }*/
                when (checkedId) {
                    R.id.CId-> showToast("đã thay đổi")
                    R.id.FId -> showToast("đã thay đổi")
                    R.id.KId -> showToast("đã thay đổi")

                }

            } else {
                if (togg.checkedButtonId == View.NO_ID) {
                    showToast("Nothing")
                }
            }
        }

        return binding.root
    }

    private fun showToast(str: String) {
        Toast.makeText(this.context, str, Toast.LENGTH_SHORT).show()
    }
}
