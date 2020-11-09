package com.zoho.countries.splash


import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProviders
import com.zoho.countries.R
import kotlinx.android.synthetic.main.activity_splash.*

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.zoho.countries.MainActivity

class SplashActivity : AppCompatActivity() {
    private val handler = Handler()
    private lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        handler?.apply {
            removeCallbacks(runnable)
            postDelayed(runnable, viewModel.getSplashHolderTime())
        }
    }

    override fun onPause() {
        handler?.apply {
            removeCallbacks(runnable)
        }
        super.onPause()
    }

    private val runnable: Runnable = Runnable() {
        var intent = viewModel.getIntent(this@SplashActivity)
        startActivity(intent!!)
        this.finish()

    }
}

