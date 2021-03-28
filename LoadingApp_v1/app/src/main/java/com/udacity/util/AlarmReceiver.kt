package com.udacity.util

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.udacity.R

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        //on receive notifies the app when the download is complete from Main...
        val notificationManager = context?.let {
            ContextCompat.getSystemServiceName(
                it,
                NotificationManager::class.java
            )
        } as NotificationManager

        if (intent != null) {
            notificationManager.sendNotification(
                    context.getText(R.string.download_ready).toString(),
                    context
            )
        }
    }
}