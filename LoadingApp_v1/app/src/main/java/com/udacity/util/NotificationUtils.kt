 package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.detail.DetailActivity

//notification id
const val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.getStringExtra("fileName")


    //create a pending intent
    val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
           PendingIntent.FLAG_UPDATE_CURRENT
    )

    //get the image for the notification
    val stonehenge = BitmapFactory.decodeResource(
        applicationContext.resources,
            R.drawable.stonehenge)

    //the large picture format
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(stonehenge)
        .bigLargeIcon(null)

    //1. get an instance of NotificationCompat.Builder
    val builder = NotificationCompat.Builder(
        applicationContext,
            applicationContext.getString(R.string.loadingbutton_notification_channel_id)
    )



    //Set the notification Icon that represents the App. Set the title, text and icon to builder
        .setSmallIcon(R.drawable.stonehenge)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

    //set the content pending intent, this is used to bring item
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

    //add style to builder
        .setStyle(bigPicStyle)
        .setLargeIcon(stonehenge)

    // call Notify with the unique ID, and builder. The ID is provided as a variable in the header.
    notify(NOTIFICATION_ID, builder.build())
}


 //cancel all notifications. this is an extension function
fun NotificationManager.cancelNotification() {
    cancelAll()
}