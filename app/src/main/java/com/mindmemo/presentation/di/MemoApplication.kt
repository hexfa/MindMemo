package com.mindmemo.presentation.di

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MemoApplication: Application() {

    private lateinit var sharedPreferences: SharedPreferences
    private var nightMode = 0

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE)
        nightMode = sharedPreferences.getInt("NightModeInt", 1)
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

}