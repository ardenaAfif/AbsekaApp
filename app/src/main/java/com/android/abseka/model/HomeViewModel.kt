package com.android.abseka.model

import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.abseka.R
import com.android.abseka.data.Absensi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class HomeViewModel : ViewModel() {
    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    fun kehadiranDatang(userId: String) {

        // Mendapatkan waktu saat tombol ditekan
        val waktu = System.currentTimeMillis()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = waktu
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val statusDatang = when {
            hour < 10 -> "ON TIME"
            hour < 11 -> "LATE"
            hour < 12 -> "ABSENT"
            else -> "Belum Absen"
        }

        val databaseRef = database.getReference("absensi").child(userId)
        val absensiData = Absensi(
            name = "Ardena",
            statusDatang = statusDatang,
            waktuDatang = waktu
        )

        // Menyimpan data
        databaseRef.push().setValue(absensiData)
            .addOnFailureListener { }
    }

    fun kehadiranPulang(userId: String) {
        // Mendapatkan waktu saat tombol ditekan
        val waktu = System.currentTimeMillis()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = waktu
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val statusPulang = when (hour) {
            in 15..16 -> "ON TIME"   // Between 15:00 and 15:59
            in 16..17 -> "LATE"      // Between 16:00 and 16:59
            else -> "Belum Absen"  // Other times
        }

        val databaseRef = database.getReference("absensi").child(userId)

        // Check if there's an existing data
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach {
                        val absensiId = it.key
                        val absensiData = it.getValue(Absensi::class.java)

                        absensiData?.let { data ->
                            data.statusPulang = statusPulang
                            data.waktuPulang = waktu

                            // Update data
                            databaseRef.child(absensiId!!).setValue(data)
                                .addOnFailureListener { }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Database Error.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getStatusRes(label: String): Int {
        return when (label) {
            "ON TIME" -> R.drawable.ket_on_time
            "LATE" -> R.drawable.ket_late
            "ABSENT" -> R.drawable.ket_absent
            else -> R.drawable.ket_not_yet_absent
        }
    }

    fun getStatusStr(label: String, resources: Resources): String {
        return when (label) {
            "ON TIME" -> resources.getString(R.string.hadir)
            "LATE" -> resources.getString(R.string.late)
            "ABSENT" -> resources.getString(R.string.ghaib)
            else -> resources.getString(R.string.not_yet_absent)
        }
    }
}