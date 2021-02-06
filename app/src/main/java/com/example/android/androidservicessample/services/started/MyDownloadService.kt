package com.example.android.androidservicessample.services.started

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

class MyDownloadService : Service() {

    companion object{
        const val TAG = "MyDownloadService"
    }

    lateinit var downloadThread: DownloadThread

    // called only once
    override fun onCreate() {
        super.onCreate()

        downloadThread = DownloadThread(applicationContext)
        downloadThread.start()

        while (!downloadThread.isDownloadHandlerInitialized)
        {

        }

        downloadThread.mDownloadHandler.myDownloadService=this

        Log.d(TAG,"service onCreate called")
    }

    // intent : same intent is received which is used to start service
    // startId : whenever the service starts via startService a unique id is also given to it
     // this method called multiple times
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(TAG,"service onStartCommand called, startId: $startId")

        //Log.d(TAG,"thread name: ${Thread.currentThread().name}")

       // Log.d("songName","song name: "+intent!!.getStringExtra(MainActivity.SONG_NAME))

        intent?.let {int->
            val songName = int.getStringExtra(MainActivity.SONG_NAME)?:"N/A"

            val resultReceiver = int.getParcelableExtra<ResultReceiver>(Intent.EXTRA_RESULT_RECEIVER)
            if (resultReceiver!=null)
                downloadThread.mDownloadHandler.resultReceiver=resultReceiver

            // As onStartCommand runs on main thread we have several ways to offload work from main thread:

            //1. Via Thread
            /*val thread = Thread {
                downloadSong(songName)
            }
            thread.start()*/

            //2. Via Handler
            val message = Message.obtain()
            message.obj=songName
            message.arg1=startId
            downloadThread.mDownloadHandler.sendMessage(message)

            // 3. Via Async Task
            /*val mySongDownloader = MySongDownloader()
            mySongDownloader.execute(songName)*/
        }

        //if process stopped so the service:
        return START_REDELIVER_INTENT   //  but START_REDELIVER_INTENT indicates that this service would restart and intent is also provided with it
        //return START_STICKY       indicates that service will restart but intent will not be delivered to onStartCommand, intent would be null
        //return START_NOT_STICKY   neither the service starts nor the intent delivered
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG,"service onBind called")
        return null // returning null means it is started service
    }

    private fun downloadSong(songName:String){
        Log.d(TAG,"run: starting download")
        try {
            Thread.sleep(4000)
        } catch (e:Exception){

        }
        Log.d(TAG,"download song: $songName Downloaded...")
    }


    // called only once
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"service onDestroy called")
    }


    class MySongDownloader : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg songs: String?): String {
            songs.forEach {songName->
                Thread.sleep(2000)
                publishProgress(songName)
            }
            return "All songs downloaded"
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            Log.d(TAG, "onProgressUpdate: Song downloaded: $values")
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.d(TAG, "onPostExecute $result")
        }

    }
}