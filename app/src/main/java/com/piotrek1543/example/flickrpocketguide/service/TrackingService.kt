package com.piotrek1543.example.flickrpocketguide.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Nullable
import com.piotrek1543.example.flickrpocketguide.NotificationUtils
import com.piotrek1543.example.flickrpocketguide.ui.TrackingActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import java.util.concurrent.TimeUnit


class TrackingService : Service() {

    private val compositeDisposable = CompositeDisposable()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notification = NotificationUtils.createNotification(this.applicationContext)
        startForeground(1, notification)
        broadcastStatus(true)

        val trackingObserver = Observable.interval(1_000, TimeUnit.MILLISECONDS)
            .doOnDispose { broadcastStatus(false) }
            .subscribeWith(TrackingObserver())

        compositeDisposable.add(trackingObserver)

        return START_NOT_STICKY
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        compositeDisposable.dispose()
    }

    private fun broadcastStatus(isRunning: Boolean) {
        val intent = Intent(TrackingActivity.ACTION_TRACKING)
        intent.putExtra(ARG_IS_RUNNING, isRunning)
        sendBroadcast(intent)
    }

    inner class TrackingObserver : DisposableObserver<Long>() {

        override fun onNext(t: Long) {
            broadcastTimer(t)
        }

        override fun onError(exception: Throwable) {
            Timber.e(exception)
        }

        override fun onComplete() {
            Timber.d("onComplete")
        }

        private fun broadcastTimer(timeInMillis: Long) {
            val i = Intent(TrackingActivity.ACTION_TRACKING)
            i.putExtra(ARG_TIMER, timeInMillis)
            sendBroadcast(i)
        }
    }

    companion object {
        const val ARG_TIMER = "timer"
        const val ARG_IS_RUNNING = "isRunning"
    }

}