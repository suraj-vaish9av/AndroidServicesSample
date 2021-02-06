package com.example.android.androidservicessample.services.started

import android.content.Context
import android.os.Looper

// creating a handler in the thread to offload data from main thread
class DownloadThread(val context: Context) : Thread()
{
    lateinit var mDownloadHandler: DownloadHandler

    override fun run() {
        Looper.prepare()
        mDownloadHandler = DownloadHandler(context)
        Looper.loop()
    }

    val isDownloadHandlerInitialized
        get() = ::mDownloadHandler.isInitialized
}