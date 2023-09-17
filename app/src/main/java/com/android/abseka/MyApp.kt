package com.android.abseka

import android.app.Application
import com.android.abseka.utils.PrefHelper
import java.util.Locale

class MyApp: Application() {

    companion object {
        var prefHelper: PrefHelper? = null
    }

    override fun onCreate() {
        super.onCreate()
        prefHelper = PrefHelper(this)
    }
}