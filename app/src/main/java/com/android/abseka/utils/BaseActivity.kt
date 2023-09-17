package com.android.abseka.utils

import android.content.Context
import android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent
import androidx.appcompat.app.AppCompatActivity
import com.android.abseka.MyApp
import java.util.Locale

object BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        val strLocale: String = MyApp.prefHelper?.getString("language") ?: "en"
        val context = LanguageContextWrapper.wrap(newBase, Locale(strLocale))
        super.attachBaseContext(context)
    }
}