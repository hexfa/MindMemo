package com.mindmemo.presentation.notification

import android.annotation.SuppressLint
import android.widget.Toast
import com.mindmemo.presentation.application.MemoApplication

@SuppressLint("StaticFieldLeak")
object NotificationService {

    private val context = MemoApplication.context

    private fun showInformation(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showError(message: String) {
        showInformation(message)
    }
}