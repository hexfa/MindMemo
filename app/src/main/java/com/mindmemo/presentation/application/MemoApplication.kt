package com.mindmemo.presentation.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MemoApplication : Application() {

    private lateinit var sharedPreferences: SharedPreferences
    private var nightMode = 0

    override fun onCreate() {
        super.onCreate()
        context = this

        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE)
        nightMode = sharedPreferences.getInt("NightModeInt", 1)
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

}