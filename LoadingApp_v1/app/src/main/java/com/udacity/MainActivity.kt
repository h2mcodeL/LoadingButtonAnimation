package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.detail.DetailActivity
import com.udacity.main.ButtonState
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var downloadManager: DownloadManager
    private var urlId: String = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    //set up these items for the download
    private lateinit var fileName: String

     var downLoadData = ""
     var item: Intent? = intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        //set the LoadingButton click listener
        custom_button.setOnClickListener {

            //check if radiobutton selected
            if(radiogroup.checkedRadioButtonId == -1) {
                Toast.makeText(applicationContext, "Please select a download item!", Toast.LENGTH_SHORT).show()
            }

            //check if a connection exists.
            val connectionExists = checkConnection(this)
            if (connectionExists) {     //if the connection exists, do the download, else

                // if the radio button items are selected, set urlId to the id of selected radio button and call download(), then set buttonstate to Loading

                when (radiogroup.checkedRadioButtonId) {
                    R.id.glidebutton -> {
                        urlId = URL_glide
                          fileName = getString(R.string.glide_title)
                        custom_button.buttonState = ButtonState.Loading
                        download()

                    }
                    R.id.loadappbutton -> {
                        urlId = URL
                            fileName = getString(R.string.loadapp)
                        custom_button.buttonState = ButtonState.Loading
                        download()
                      //  downloadUrl
                    }

                    R.id.retrofitbutton -> {
                        urlId = URL_retrofit
                            fileName = getString(R.string.retrofit_link)
                        download()
                        custom_button.buttonState = ButtonState.Loading
                    }

                    else -> Toast.makeText(applicationContext, "Please select a download item!", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(applicationContext, "Connection Required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id == downloadID) {
                Toast.makeText(applicationContext, "SUCCESS", Toast.LENGTH_SHORT).show()
                custom_button.buttonState = ButtonState.Completed
                //this is where the pendingIntent should be called.
            } else {
                Toast.makeText(applicationContext, "FAILED", Toast.LENGTH_SHORT).show()
            }

            //get an instance of notificationManager
            notificationManager = ContextCompat.getSystemService(
                    applicationContext, NotificationManager::class.java) as NotificationManager

            //pass the data on ###################
            item = Intent(applicationContext, DetailActivity::class.java)
            item!!.putExtra("fileName", fileName)

            Log.i("URL", fileName)

            //create a pending intent
            pendingIntent = PendingIntent.getActivity(applicationContext, 1, item, PendingIntent.FLAG_UPDATE_CURRENT)

            //use the notification in the download
            createChannel(
                    getString(R.string.loadingbutton_notification_channel_id),
                    getString(R.string.download_notification_channel_name))
            ButtonState.Completed
            notificationManager.sendNotification(urlId, applicationContext)

        }
    }

    //check if there is an internet connection
    private fun checkConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager // determine type of connection
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            }
        }
        return false
    }

    private fun download() {
        val request =
                DownloadManager.Request(Uri.parse(urlId))
                        .setDescription(getString(R.string.app_description))
                        .setRequiresCharging(false)
                        .setAllowedOverMetered(true)
                        .setAllowedOverRoaming(true)
                        .setTitle(fileName)     //set download file name for selected item
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        //once download is complete, create dlmanager and cast to DownloadManager
                    downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    //create a notifications channel
    private fun createChannel(channelId: String, channelName: String) {
        //check for phones with a build API lower than O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,      //need to use channelId, to get notification
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor =  Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Your download is complete"

            notificationManager = this.getSystemService(        //we use this as we are already inside an activity
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)


        }
    }

    companion object {
        private const val CHANNEL_ID = "channelId"  //this is for the notification
        //url selection for download.
        private const val URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_glide = "https://github.com/bumptech/glide"
        private const val URL_retrofit = "https://github.com/square/retrofit"
    }

override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(receiver)
    }
}