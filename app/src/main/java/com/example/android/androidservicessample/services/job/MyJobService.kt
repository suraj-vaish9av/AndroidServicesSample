package com.example.android.androidservicessample.services.job

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MyJobService : JobService() {

    val TAG="MyJobService"

    var isJobCancelled=false

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG,"onCreate called")

    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG,"onStartJob called")

        var i=0

        // starting a bg thread because onStartJob also runs on main thread
        Thread{
            while (i<=10)
            {
                if (isJobCancelled)
                    return@Thread
                Thread.sleep(1000)
                Log.d(TAG,"progress: $i")
                i++
            }
            Log.d(TAG,"job completed")

            jobFinished(params,false)   // finish job to free acquired wakelock

        }.start()

        return true  // is onStartJob running a bg thread to complete its work?
        // , use true if yes so that even if the control reached at the end but the job still doing something on bg thread
    }

    // called if the constraint is removed at time onStartJob was executing
    // suppose a constraint is wifi-on, and during the execution of onStartJob, wifi is turned off, this method will be called
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG,"onStopJob called")
        isJobCancelled=true
        return false // retry?, use true if you want to reschedule
        // if your job is running in every 30 min, because of some reason it is stopped after 5 mins then
        // in that case you may not want to retry because it will automatically run after 25 mins
        // yeah but if it is running once a week you can go with retry true
    }

}