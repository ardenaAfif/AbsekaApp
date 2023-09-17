package com.android.abseka.utils

import android.content.Context

class PrefHelper(context: Context) {

    private val prefHelper = context.getSharedPreferences("pref_app", Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        prefHelper.edit().putString(key, value).apply()
    }

    fun getString(key: String): String {
        return prefHelper.getString(key, "") ?: ""
    }
}