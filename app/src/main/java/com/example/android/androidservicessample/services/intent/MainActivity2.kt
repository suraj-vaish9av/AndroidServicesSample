package com.example.android.androidservicessample.services.intent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.androidservicessample.R
import com.example.android.androidservicessample.services.intent.MyIntentService.Companion.ACTION_SONG_DOWNLOADED
import com.example.android.androidservicessample.services.started.MainActivity

class MainActivity2 : AppCompatActivity() {

    private val txtSongsDownloaded: TextView by lazy { findViewById<TextView>(R.id.txtSongsDownloaded) }
    private val listOfSong = listOf("Hotel California","Private Emotion","Angel of the morning","Black & White","Riders on the storm")

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val songName = intent?.getStringExtra(MainActivity.SONG_NAME)?:"N/A"
            appendData(songName)
        }
    }

    fun appendData(songName:String){
        txtSongsDownloaded.append("\n"+songName)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(broadcastReceiver, IntentFilter(ACTION_SONG_DOWNLOADED))
    }

    fun onStartIntentService(view: View) {
        listOfSong.forEach {songName->

            val intent = Intent(applicationContext, MyIntentService::class.java)
            intent.putExtra(MainActivity.SONG_NAME,songName)
            startService(intent)

        }
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(broadcastReceiver)
    }
}