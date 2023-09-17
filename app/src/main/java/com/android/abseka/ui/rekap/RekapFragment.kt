package com.android.abseka.ui.rekap

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.abseka.R
import com.android.abseka.data.Absensi
import com.android.abseka.databinding.FragmentRekapBinding
import com.android.abseka.model.HomeViewModel
import com.android.abseka.model.RekapViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class RekapFragment : Fragment() {

    private lateinit var binding: FragmentRekapBinding
    private val attendanceData = mutableListOf<Absensi>()
    private lateinit var rekapViewModel: RekapViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRekapBinding.inflate(inflater, container, false)

        attendanceData.addAll(
            listOf(
                Absensi("2023-08-01", "Hadir"),
                Absensi("2023-08-02", "Absen"),
                Absensi("2023-08-03", "Terlambat"),
                // ...
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rekapViewModel = ViewModelProvider(this).get(RekapViewModel::class.java)

        getToday()

        val today = Calendar.getInstance()
        binding.apply {
            calendarView.date = today.timeInMillis

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)

                // Check if the selected date is Friday
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    calendarView.setDateTextAppearance(
                        R.style.BlueFriday
                    )
                } else {
                    calendarView.setDateTextAppearance(
                        android.R.style.TextAppearance_DeviceDefault
                    )
                }

            }
        }

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
    }

    private fun getToday() {
        val today = Calendar.getInstance()

        binding.apply {
            btnToday.setOnClickListener {
                calendarView.date = today.timeInMillis
            }
        }
    }

    private fun findDateView(
        calendarView: CalendarView,
        year: Int,
        month: Int,
        day: Int
    ): TextView? {
        val childCount = calendarView.childCount
        for (i in 0 until childCount) {
            val child = calendarView.getChildAt(i)
            if (child is ViewGroup) {
                val dateView = findDateViewInGroup(child, year, month, day)
                if (dateView != null) {
                    return dateView
                }
            }
        }
        return null
    }

    private fun findDateViewInGroup(
        viewGroup: ViewGroup,
        year: Int,
        month: Int,
        day: Int
    ): TextView? {
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is TextView) {
                val text = child.text.toString()
                if (text == day.toString()) {
                    return child
                }
            }
        }
        return null
    }


}