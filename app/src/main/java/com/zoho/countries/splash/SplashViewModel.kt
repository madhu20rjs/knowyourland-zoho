package com.zoho.countries.splash

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.zoho.countries.MainActivity


import java.util.concurrent.TimeUnit

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private var startTime: Long = 0

    init {
        startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    }

    fun getSplashHolderTime(): Long {
        var temp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - startTime
        if (temp in 0..5000) {
            return (5000 - temp)
        } else {
            return 1000
        }
    }

    fun getIntent(activity: SplashActivity): Intent {
        var intent: Intent? = null
        intent = Intent(activity, MainActivity::class.java)
        return intent;
    }
}

