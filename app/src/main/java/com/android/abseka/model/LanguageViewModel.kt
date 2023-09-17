package com.android.abseka.model

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.abseka.MyApp
import com.android.abseka.R
import com.android.abseka.utils.DialogChangeLanguage

class LanguageViewModel(application: Application) : AndroidViewModel(application) {

    var title = MutableLiveData("")

    fun setup(activity: Activity) {
        title.postValue(activity.getString(R.string.app_name))
    }

    fun changeLanguage(activity: Activity) {

        DialogChangeLanguage(activity, {
            MyApp.prefHelper?.setString("language", "en")
            restartApp(activity)
        }, {
            MyApp.prefHelper?.setString("language", "in")
            restartApp(activity)
        }, {
            MyApp.prefHelper?.setString("language", "ar")
            restartApp(activity)
        })
    }

    private fun restartApp(activity: Activity) {
        val intent =
            activity.baseContext.packageManager.getLaunchIntentForPackage(activity.baseContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()
    }
}