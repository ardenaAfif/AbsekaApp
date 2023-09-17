package com.android.abseka.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.abseka.R
import com.android.abseka.databinding.FragmentHomeBinding
import com.android.abseka.model.HomeViewModel
import com.android.abseka.model.RekapViewModel
import com.android.abseka.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var rekapViewModel: RekapViewModel
    private var isKlikAbsenClicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        rekapViewModel = ViewModelProvider(this).get(RekapViewModel::class.java)

        // Observe the status counts and update the UI when it changes
        rekapViewModel.statusCounts.observe(viewLifecycleOwner, Observer { counts ->
            // Update the respective TextViews with the counts
           binding.apply {
               totalHadir.text = counts["ON TIME"].toString()
               totalLate.text = counts["LATE"].toString()
               totalAbsent.text = counts["ABSENT"].toString()
           }
        })

        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        if (userId != null) {
            rekapViewModel.updateStatusCount(userId)
        }

        // Mendapatkan waktu saat tombol ditekan
        val waktuSekarang = Calendar.getInstance()
        val hour = waktuSekarang.get(Calendar.HOUR_OF_DAY)

        val waktu = System.currentTimeMillis()
        waktuSekarang.timeInMillis = waktu

        binding.apply {
            if (hour >= 18 || hour < 0) {
                klikAbsen.setOnClickListener {
                    isKlikAbsenClicked = true
                    Toast.makeText(context, R.string.office_hour, Toast.LENGTH_SHORT).show()
                }
            } else {
                klikAbsen.setOnClickListener {
                    isKlikAbsenClicked = true
                    if (userId != null) {
                        // Check if it's after 6 AM or before 8 AM

                        viewModel.kehadiranDatang(userId)
                        Toast.makeText(context, R.string.success_absen, Toast.LENGTH_SHORT).show()

                        val statusDatang = when {
                            hour < 10 -> "ON TIME"
                            hour < 11 -> "LATE"
                            hour < 12 -> "ABSENT"
                            else -> "Belum Absen"
                        }

                        // update UI
                        binding.apply {
                            tvTimeDatang.text =
                                SimpleDateFormat("HH:mm", Locale.getDefault()).format(waktu)

                            val statusDatangRes = viewModel.getStatusRes(statusDatang)
                            val statusDatangStr = viewModel.getStatusStr(statusDatang, resources)

                            ivDatangStatus.setImageResource(statusDatangRes)
                            tvDatangStatus.text = statusDatangStr
                        }

                        disableButton(klikAbsen)
                        Toast.makeText(context, "Antum sudah absen", Toast.LENGTH_SHORT).show()

                        // Schedule re-enabling the button at 6 AM
                        val currentTimeMillis = System.currentTimeMillis()
                        val sixAMCalendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, 6)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            // If it's already past 6 AM, schedule for the next day
                            if (hour > 6) {
                                add(Calendar.DAY_OF_YEAR, 1)
                            }
                        }
                        val timeUntilSixAM = sixAMCalendar.timeInMillis - currentTimeMillis

                        Handler().postDelayed({
                            // Enable the button at 6 AM
                            klikAbsen.isEnabled = true
                        }, timeUntilSixAM)
                    } else {
                        Intent(context, LoginActivity::class.java).also {
                            startActivity(it)
                        }
                    }
                }
            }

            if (hour >= 18 || hour < 8) {
                klikAbsenPulang.setOnClickListener {
                    Toast.makeText(context, R.string.office_hour, Toast.LENGTH_SHORT).show()
                }
            } else {
                klikAbsenPulang.setOnClickListener {
                    if (!isKlikAbsenClicked) {
                        Toast.makeText(context, R.string.klik_absen_first, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    klikAbsenPulang.isEnabled = false
                    if (userId != null) {
                        viewModel.kehadiranPulang(userId)
                        Toast.makeText(context, R.string.success_absen, Toast.LENGTH_SHORT).show()

                        val statusPulang = when (hour) {
                            in 15..16 -> "ON TIME"   // Between 15:00 and 15:59
                            in 16..17 -> "LATE"      // Between 16:00 and 16:59
                            else -> "Belum Absen"  // Other times
                        }

                        // update UI
                        binding.apply {
                            tvTimePulang.text =
                                SimpleDateFormat("HH:mm", Locale.getDefault()).format(waktu)

                            val statusPulangRes = viewModel.getStatusRes(statusPulang)
                            val statusPulangStr = viewModel.getStatusStr(statusPulang, resources)

                            ivPulangStatus.setImageResource(statusPulangRes)
                            tvPulangStatus.text = statusPulangStr
                        }
                    }

                }
            }
        }

        updateCurrentDate()
    }

    // Update current date
    private fun updateCurrentDate() {
        val currentDate = getCurrentDate()
        binding.tvDateHome.text = currentDate
    }

    private fun getCurrentDate(): String {
        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault())
        return dateFormat.format(Date())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun disableButton(button: LinearLayout) {
        button.isEnabled = false
        Toast.makeText(context, R.string.success_absen, Toast.LENGTH_SHORT).show()
    }
}