package com.example.android.androidservicessample.services.bound

import android.app.Service
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.androidservicessample.R

class MainActivity3 : AppCompatActivity() {

    val TAG = "MyMainActivity3"

    var hasBounded=false
    var myBoundService:MyBoundService?=null
    private val txtMessage:TextView by lazy { findViewById(R.id.txtMessage) }
    val btnPausePlay:Button by lazy { findViewById(R.id.btnPausePlay) }

    /**
     * creating an anonymous class to implement ServiceConnection interface
     */
    private val serviceConnection = object : ServiceConnection {

        /**
         * this method would be called after service binded to the activity and gives us the Binder reference which is returned by onBind method
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG,"onServiceConnected")
            (service as? MyBoundService.MyBinder)?.let {myBinder ->
                hasBounded=true
                myBoundService=myBinder.getService()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG,"onServiceDisconnected")
        }

    }

    /**
     * just to receive data from service
     */
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val isSongCompletelyPlayed= intent?.getBooleanExtra(MyBoundService.EXTRA_IS_COMPLETED,false)?:false
            if (isSongCompletelyPlayed)
                btnPausePlay.text = "Play"
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(applicationContext,MyBoundService::class.java)
        bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE) // this method will start the service and also trigger onBind method

        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(broadcastReceiver, IntentFilter(MyBoundService.ACTION_SONG_COMPLETED))
    }

    fun onPausePlayClicked(view: View) {

        if (hasBounded){
            if (myBoundService?.isPlaying == true)
            {
                btnPausePlay.text = "Play"
                myBoundService?.pause()
            }
            else{
                // starting this service as started service also because we want a behaviour that; service should keep running the player
                    // even if the bounded component has called unbindService, because even user minimizes the app or pressing the back button service should keep playing music
                val intent = Intent(applicationContext,MyBoundService::class.java)
                startService(intent)

                btnPausePlay.text = "Pause"
                myBoundService?.play()
            }
        }

        /*val someData = myBoundService?.getData()
        txtMessage.append("\n$someData")*/
    }

    override fun onStop() {
        super.onStop()

        unbindService(serviceConnection)    // closing the connection with service and would trigger onUnbind method of the service

        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(broadcastReceiver)
    }

}