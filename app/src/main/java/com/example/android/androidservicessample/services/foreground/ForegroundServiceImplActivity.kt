package com.example.android.androidservicessample.services.foreground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.android.androidservicessample.R

class ForegroundServiceImplActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foreground_service_impl)
    }

    fun startForeground(view: View) {
        val intent=Intent(applicationContext,MyForegroundService::class.java)
        // when calling this method MyForegroundService should call startForeground under 5 seconds
        ContextCompat.startForegroundService(applicationContext,intent)
    }
}