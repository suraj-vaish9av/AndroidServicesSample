package com.example.android.androidservicessample.services.intent

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.androidservicessample.services.started.MainActivity
import com.example.android.androidservicessample.services.started.MyDownloadService

// This is same as started service, but it uses handler, thread looper under the hood and gives us the onHandleIntent method which runs on bg thread
class MyIntentService : IntentService("MyIntentService") {

    val TAG="MyIntentService"

    companion object{
        const val ACTION_SONG_DOWNLOADED="ACTION_SONG_DOWNLOADED"
    }

    init {
        // if set true it will work same as Service.START_REDELIVER_INTENT otherwise Service.START_NOT_STICKY
        setIntentRedelivery(true)
    }

    // called only once
    override fun onCreate() {
        super.onCreate()

        Log.d(TAG,"onCreate, thread: ${Thread.currentThread().name}")
    }

    // onHandleIntent method runs on bg thread note that onStartCommand is not available here,
    // because that is already implemented by IntentService class the parent
    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG,"onHandleIntent, thread: ${Thread.currentThread().name}")

        intent?.let {it->
            val songName = it.getStringExtra(MainActivity.SONG_NAME)?:""
            downloadSong(songName)

            sendDataToTheUI(songName)
        }
    }

    private fun downloadSong(songName:String){
        Log.d(MyDownloadService.TAG,"run: starting download")
        try {
            Thread.sleep(4000)
        } catch (e:Exception){

        }
        Log.d(MyDownloadService.TAG,"download song: $songName Downloaded...")
    }

    // Using LocalBroadcastManager to sending data from service to activity
    private fun sendDataToTheUI(songName: String){
        val intent = Intent(ACTION_SONG_DOWNLOADED)
        intent.putExtra(MainActivity.SONG_NAME,songName)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    // called only once
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy, thread: ${Thread.currentThread().name}")
    }

}