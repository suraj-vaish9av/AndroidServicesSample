package com.example.android.androidservicessample.services.started

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.ResultReceiver
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class DownloadHandler(private val context: Context) : Handler()
{
    companion object{
        const val ACTION_BROADCAST_SONG_DOWNLOADED="ACTION_BROADCAST_SONG_DOWNLOADED"
    }

    lateinit var myDownloadService: MyDownloadService
    var resultReceiver:ResultReceiver?=null


    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)

        downloadSong(msg.obj.toString())
        val result = myDownloadService.stopSelfResult(msg.arg1)     // stopping the service by id, if the given serviceId is the max then the entire service would be shutdown,
        // and this result would be return as true
        Log.d(MyDownloadService.TAG, "service stopped: $result, startId: ${msg.arg1}")


        // To send data back to activity, there are two ways

        // 1. Result Receiver
        val bundle = Bundle()
        bundle.putString(MainActivity.SONG_NAME,msg.obj.toString())
        resultReceiver?.send(Activity.RESULT_OK,bundle)


        // 2. Broadcast Receiver
        val intent = Intent(ACTION_BROADCAST_SONG_DOWNLOADED)
        intent.putExtra(MainActivity.SONG_NAME,msg.obj.toString())
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun downloadSong(songName:String){
        Log.d(MyDownloadService.TAG,"run: starting download")
        try {
            Thread.sleep(4000)
        } catch (e:Exception){

        }
        Log.d(MyDownloadService.TAG,"download song: $songName Downloaded...")
    }


}