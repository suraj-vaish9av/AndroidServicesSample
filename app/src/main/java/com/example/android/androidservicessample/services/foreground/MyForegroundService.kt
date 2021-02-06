package com.example.android.androidservicessample.services.foreground

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.android.androidservicessample.R

/**
 * If an app has a foreground service running even if that app is not being currently used by user
 * it would be considered as foreground app because it has a foreground service running
 *
 * Foreground service attached to a notification
 */
class MyForegroundService : Service() {

    val TAG="MyForegroundService"

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate")
    }

    /**
     * also runs on main thread
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()

        val thread = Thread {
            var i=0
            while (i<=10)
            {
                Log.d(TAG, "progress $i")
                Thread.sleep(1000)
                i++
            }
            Log.d(TAG, "download complete")

            // stops the foreground service and remove the notification if given true
            stopForeground(true)
            // stops the foreground service
            stopSelf()
        }

        thread.start()



        return START_STICKY  // would start the service again if process is killed with intent as null to onStartCommand method
    }

    private fun showNotification(){
        // just preparing a notification
        val builder = NotificationCompat.Builder(applicationContext,"MyChannel")
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("This is a service notification")
                .setContentTitle("Song Playing")
                .setOngoing(true)


        // will start this service as foreground service and also fire a notification with the given id
        startForeground(123,builder.build())
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }



}