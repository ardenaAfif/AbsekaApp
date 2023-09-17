package com.android.abseka.ui.language

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.android.abseka.R
import com.android.abseka.databinding.ActivityLanguageBinding
import com.android.abseka.model.LanguageViewModel
import java.util.Locale

class LanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageBinding
    private lateinit var viewModel: LanguageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[LanguageViewModel::class.java]
        binding.main = viewModel
        viewModel.setup(this)

        setupToolbar()
        setupButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupButton() {
        binding.btnChangeLanguage.setOnClickListener {
            viewModel.changeLanguage(this)
        }
    }

    private fun updateLanguage(selectedLanguage: String) {
        val context = this.applicationContext
        val locale = Locale(selectedLanguage)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}