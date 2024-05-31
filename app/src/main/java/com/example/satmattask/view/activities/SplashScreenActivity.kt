package com.example.satmattask.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.satmattask.R
import com.example.satmattask.auth.SignInActivity
import com.example.satmattask.auth.SignUpActivity
import com.example.satmattask.utils.Utils
import com.google.firebase.auth.FirebaseAuth


class SplashScreenActivity : AppCompatActivity() {
    private var SPLASH_SCREEN_DELAY: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = FirebaseAuth.getInstance().currentUser?.uid
            if (currentUser != null) {
                val isEmailVerified = FirebaseAuth.getInstance().currentUser?.isEmailVerified
                if (isEmailVerified == true) {
                    startActivity(Intent(this@SplashScreenActivity, DashboardActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@SplashScreenActivity, SignInActivity::class.java))
                    finish()
                }
            } else {
                startActivity(Intent(this@SplashScreenActivity, SignInActivity::class.java))
                finish()
            }

        }, SPLASH_SCREEN_DELAY)
    }
}