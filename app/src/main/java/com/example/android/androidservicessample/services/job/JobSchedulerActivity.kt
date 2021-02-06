package com.example.android.androidservicessample.services.job

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.android.androidservicessample.R

class JobSchedulerActivity : AppCompatActivity() {

    val JOB_ID=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_scheduler)
    }

    fun startJob(view: View) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val jobScheduler = this.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            val jobInfo = JobInfo.Builder(JOB_ID, ComponentName(this, MyJobService::class.java))
                .setMinimumLatency(0)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .build()

            val result = jobScheduler.schedule(jobInfo)
            if (result==JobScheduler.RESULT_SUCCESS)
            {
                Log.d("MyJobService","jobService scheduled")
            }
            else{
                Log.d("MyJobService","jobService not scheduled")
            }


        } else {
            Toast.makeText(applicationContext, "Can't run job on devices below lollipop", Toast.LENGTH_SHORT).show()
        }

    }

    fun stopJob(view: View) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val jobScheduler = this.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.cancel(JOB_ID)

        } else {
            Toast.makeText(applicationContext, "Can't use job on devices below lollipop", Toast.LENGTH_SHORT).show()
        }

    }
}