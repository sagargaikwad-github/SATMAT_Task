package com.example.satmattask.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import com.example.satmattask.R
import com.example.satmattask.databinding.FragmentReportsBinding
import com.example.satmattask.view.activities.AllReportsActivity
import com.example.satmattask.view.activities.MobileRechargeActivity


class ReportsFragment : Fragment() {
    private lateinit var binding: FragmentReportsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportsBinding.inflate(layoutInflater)

        binding.apply {
            reportsToolbar.setNavigationOnClickListener {
                val fragmentTransaction: FragmentTransaction = fragmentManager?.beginTransaction()!!
                fragmentTransaction.replace(R.id.dashboardFrameLayout, HomeFragment())
                fragmentTransaction.commit()
            }

            reportsAllReports.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        AllReportsActivity::class.java
                    )
                )
            }
        }
        return binding.root
    }
}