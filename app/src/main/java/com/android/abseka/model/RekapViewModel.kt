package com.android.abseka.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.abseka.data.Absensi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RekapViewModel : ViewModel() {

    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    // Add a LiveData to hold the counts
    private val _statusCounts = MutableLiveData<Map<String, Int>>()
    val statusCounts: LiveData<Map<String, Int>> get() = _statusCounts

    // Function to fetch and update the status counts
    fun updateStatusCount(userId: String) {
        val databaseRef = database.getReference("absensi").child(userId)

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                val counts = mutableMapOf<String, Int>(
                    "ON TIME" to 0,
                    "LATE" to 0,
                    "ABSENT" to 0,
                    "Belum Absen" to 0
                )

                for (childSnapshot in snapshot.children) {
                    val absensi = childSnapshot.getValue(Absensi::class.java)
                    absensi?.let {
//                        counts[it.statusDatang] = counts.getOrDefault(it.statusDatang, 0) + 1
                        counts[it.statusDatang ?: "Belum Absen"] =
                            counts.getOrDefault(it.statusDatang ?: "Belum Absen", 0) + 1

                    }
                }
                _statusCounts.value = counts
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}