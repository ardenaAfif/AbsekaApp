package com.android.abseka.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.android.abseka.R
import com.android.abseka.databinding.DialogChangeLanguageBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun DialogChangeLanguage (context: Context, onEnglish: () -> Unit, onIndonesia: () -> Unit, onArabic: () -> Unit) {
    var alertDialog: AlertDialog? = null
    val builder: MaterialAlertDialogBuilder?
    val binding = DialogChangeLanguageBinding.inflate(LayoutInflater.from(context))
    binding.tvDialogEnglish.setOnClickListener {
        alertDialog?.dismiss()
        onEnglish()
    }
    binding.tvDialogIndonesia.setOnClickListener {
        alertDialog?.dismiss()
        onArabic()
    }

    binding.tvDialogArab.setOnClickListener {
        alertDialog?.dismiss()
        onIndonesia()
    }
    builder = MaterialAlertDialogBuilder(context, R.style.DialogTheme)
        .setView(binding.root)
    if (builder != null) {
        alertDialog = builder.show()
    }
}