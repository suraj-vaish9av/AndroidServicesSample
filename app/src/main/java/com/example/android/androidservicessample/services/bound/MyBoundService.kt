package com.example.android.androidservicessample.services.bound

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.androidservicessample.R

/**
 * A service would be called if that is bounded to a component
 * if we are implementing a bound service then it should return Binder reference from onBind
 * multiple component can be bound to single BoundService and that would be destroyed when and the bound component has called unBind method with this service
 */
class MyBoundService : Service() {

    companion object{
        val TAG = "MyBoundService"
        const val ACTION_SONG_COMPLETED="ACTION_SONG_COMPLETED"
        const val EXTRA_IS_COMPLETED="EXTRA_IS_COMPLETED"
    }


    lateinit var mediaPlayer : MediaPlayer

    /**
     * Binder is child of IBinder
     * we are creating this MyBinder class and would return it's reference from onBind and that would be received to the component(in ServiceConnection) who called bindService
     */
    inner class MyBinder : Binder()
    {
        fun getService()= this@MyBoundService   // just returning service reference
    }

    /**
     * Just creating a MyBinder reference
     */
    private val myBinder by lazy { MyBinder() }

    /**
     * called only once
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate called")

        mediaPlayer=MediaPlayer.create(applicationContext, R.raw.jack_sparrow_remix)

        mediaPlayer.setOnCompletionListener {
            val intent = Intent(ACTION_SONG_COMPLETED)
            intent.putExtra(EXTRA_IS_COMPLETED,true)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand called")
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * called when component call bindService with this service class
     * will return the Binder reference
     */
    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG,"onBind called")
        return myBinder
    }

    /**
     * when onBind called again by same component
     * called only if onUnbind method returns true
     */
    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG,"onRebind called")
    }

    /**
     * called when component call unbindService with this service class
     */
    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG,"onUnbind called")
        return true
    }

    /**
     * called only once
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy called")
    }

    fun getData() = "Some data from service"

    val isPlaying:Boolean
        get() = mediaPlayer.isPlaying

    fun play() = mediaPlayer.start()

    fun pause() = mediaPlayer.pause()

}