package com.example.satmattask.view.activities

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.satmattask.R
import com.example.satmattask.adapter.MobileRechargePagerAdapter
import com.example.satmattask.databinding.ActivityMobileRechargeBinding

class MobileRechargeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMobileRechargeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMobileRechargeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            rechargeViewPager.adapter = MobileRechargePagerAdapter(supportFragmentManager)
            rechargeTabLayout.setupWithViewPager(rechargeViewPager)

            rechargeToolbar.setNavigationOnClickListener {
                finish()
            }
        }
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