package com.example.noteappcleanarchitecture.data.utils

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

fun Spinner.setupListWithAdapter(list: MutableList<String>, callback: (String) -> Unit) {
    val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, list)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    this.adapter = adapter
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            callback(list[p2])
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }
}

fun MutableList<out Any>.getIndexFromList(item: Any): Int {
    var index = 0
    for (i in this.indices) {
        if (this[i] == item) {
            index = i
            break
        }
    }
    return index
}