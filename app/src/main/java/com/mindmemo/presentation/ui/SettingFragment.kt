package com.mindmemo.presentation.ui

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.makeramen.roundedimageview.BuildConfig
import com.mindmemo.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private lateinit var fragmentSettingBinding: FragmentSettingBinding

    lateinit var sharedPreferences: SharedPreferences
    var NightMode = 0
    lateinit var editor: SharedPreferences.Editor
    private var nightMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentSettingBinding = FragmentSettingBinding.inflate(layoutInflater, container, false)

        showVersionCode()
        return fragmentSettingBinding.root
    }

    override fun onResume() {
        super.onResume()
        // Save switch state in shared preferences
        val sharedPreferences2: SharedPreferences =
            requireContext().getSharedPreferences("save", MODE_PRIVATE)
        fragmentSettingBinding.switcher.isChecked = sharedPreferences2.getBoolean("value", false)
        handleMode()
    }

    private fun showVersionCode() {
        fragmentSettingBinding.tvShowVersionCode.text = BuildConfig.VERSION_NAME
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        NightMode = AppCompatDelegate.getDefaultNightMode()

        sharedPreferences = requireContext().getSharedPreferences("SharedPrefs", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        editor.putInt("NightModeInt", NightMode)
        editor.apply()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleMode() {
        if (nightMode) {
            fragmentSettingBinding.switcher.isChecked = true
        }
//
        fragmentSettingBinding.switcher.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                val editor2: SharedPreferences.Editor =
                    requireContext().getSharedPreferences("save", MODE_PRIVATE).edit()
                editor2.putBoolean("value", true)
                editor2.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                val editor2: SharedPreferences.Editor =
                    requireContext().getSharedPreferences("save", MODE_PRIVATE).edit()
                editor2.putBoolean("value", false)
                editor2.apply()
            }
        }
    }
}
