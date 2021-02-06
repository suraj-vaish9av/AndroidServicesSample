package com.example.android.androidservicessample.services.started

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.androidservicessample.R

class MainActivity : AppCompatActivity() {
    companion object{
        const val SONG_NAME="SONG_NAME"
    }

    private val txtSongsDownloaded: TextView by lazy { findViewById<TextView>(R.id.txtSongsDownloaded) }
    private val listOfSong = listOf("Hotel California","Private Emotion","Angel of the morning","Black & White","Riders on the storm")

    //  BroadcastReceiver to receive the data sent by service
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val songName = intent?.getStringExtra(SONG_NAME)?:"N/A"
            appendData(songName)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        // registering the broadcast receiver via LocalBroadcastManager,
        // with the action given as IntentFilter so that broadcastReceiver will only receive messages for DownloadHandler.ACTION_BROADCAST_SONG_DOWNLOADED
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(broadcastReceiver,
            IntentFilter(DownloadHandler.ACTION_BROADCAST_SONG_DOWNLOADED)
        )
    }


    // for started service
    fun onStartedService(view: View) {

        val resultReceiver = MyResultReceiver(null)

        listOfSong.forEach {songName->
            val intent = Intent(applicationContext, MyDownloadService::class.java)
            intent.putExtra(SONG_NAME,songName)
            intent.putExtra(Intent.EXTRA_RESULT_RECEIVER,resultReceiver)    // here passing result receiver with the intent
            startService(intent)
        }

    }

    fun onStopService(view: View) {

        // stopping the service
        val intent = Intent(applicationContext, MyDownloadService::class.java)
        stopService(intent)

    }

    override fun onStop() {
        super.onStop()

        // stopping from listing
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(broadcastReceiver)
    }

    // Result receiver to receive the data sent by service
    inner class MyResultReceiver(handler: Handler?) : ResultReceiver(handler)
    {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)
            /*val songName = resultData?.getString(SONG_NAME)?:"N/A"
            Log.d("onReceiveResult","resultCode: $resultCode, songName: $songName")

            Log.d("onReceiveResult","thread: ${Thread.currentThread().name}")

            runOnUiThread {
                appendData(songName)
            }*/
        }
    }

    fun appendData(songName:String){
        txtSongsDownloaded.append("\n"+songName)
    }
}