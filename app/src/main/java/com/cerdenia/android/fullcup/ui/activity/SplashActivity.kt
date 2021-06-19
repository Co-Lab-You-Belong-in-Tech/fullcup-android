package com.cerdenia.android.fullcup.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(when {
                FullCupPreferences.isNewUser -> IntroActivity.newIntent(this)
                FullCupPreferences.isOnboarded -> MainActivity.newIntent(this)
                else -> OnboardingActivity.newIntent(this)
            })
            finish()
        }, 1000)
    }
}