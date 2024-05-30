package com.example.satmattask

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.satmattask.auth.SignUpActivity


class SplashScreenActivity : AppCompatActivity() {
    private var SPLASH_SCREEN_DELAY: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashScreenActivity, SignUpActivity::class.java))
            finish()
        }, SPLASH_SCREEN_DELAY)
    }
}