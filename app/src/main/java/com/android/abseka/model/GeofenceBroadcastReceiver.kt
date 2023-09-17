package com.android.abseka.model

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.abseka.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                Log.e("GeofenceReceiver", "Error: ${geofencingEvent.errorCode}")
                return
            }
        }

        val transitionType = geofencingEvent?.geofenceTransition
        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
            showNotification(context)
        }
    }

    private fun showNotification(context: Context?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, "001")
            .setContentTitle("Zona Absen")
            .setContentText("Antum masuk zona absensi... Absen sekarang...")
            .setSmallIcon(R.drawable.abseka_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()

        notificationManager.notify(1, notification)
    }
}