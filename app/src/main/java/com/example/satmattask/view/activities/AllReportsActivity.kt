package com.example.satmattask.view.activities

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import com.example.satmattask.R
import com.example.satmattask.view.fragments.RechargeHistoryFragment

class AllReportsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        statusBarColor()
        setContentView(R.layout.activity_all_reports)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.allReports_FrameLayout, RechargeHistoryFragment())
        fragmentTransaction.commit()
    }

    fun statusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorPrimaryStatusBar)
        }
    }
}