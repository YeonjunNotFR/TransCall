package com.youhajun.transcall

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.youhajun.core.network.BuildConfig
import com.youhajun.core.notification.NotificationService
import com.youhajun.feature.call.service.CallForegroundService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class TransCallApplication() : Application(), Configuration.Provider {

    @Inject
    lateinit var notificationService: NotificationService
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerCoroutineContext(Dispatchers.IO)
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()

        notificationService.channelInit()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                stopService(Intent(this, CallForegroundService::class.java))
            } catch (e: Exception) {
                Timber.tag("CrashHandler").e(e, "Failed to stop service")
            }
        }
    }
}