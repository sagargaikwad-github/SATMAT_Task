package com.example.satmattask.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.satmattask.Interface.OnDateSelectedListener
import com.example.satmattask.R
import com.example.satmattask.adapter.RechargeHistoryAdapter
import com.example.satmattask.databinding.FragmentRechargeHistoryBinding
import com.example.satmattask.model.reports.RechargeHistory
import com.example.satmattask.utils.Utils
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date
import java.util.Locale
import java.util.Random


class RechargeHistoryFragment : Fragment(), OnDateSelectedListener {
    private lateinit var binding: FragmentRechargeHistoryBinding
    private lateinit var rechargeHistoryAdapter: RechargeHistoryAdapter
    private lateinit var rechargeHistoryList: ArrayList<RechargeHistory>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.allReportsToolbar)
        toolbar.title = "Recharge History"

        toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRechargeHistoryBinding.inflate(layoutInflater)

        rechargeHistoryAdapter = RechargeHistoryAdapter()
        binding.rechargeHistoryRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rechargeHistoryAdapter
        }

        makeRechargeHistoryData()

        binding.apply {
            rechargeHistoryFromDateTV.text =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                    Date()
                )

            rechargeHistoryToDateTV.text =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                    Date()
                )


            rechargeHistoryFromDate.setOnClickListener {
                val currentSelectedDate = rechargeHistoryFromDateTV.text.toString()
                Utils.showDatePickerDialog(
                    requireContext(),
                    this@RechargeHistoryFragment,
                    "FromDate", currentSelectedDate
                )
            }

            rechargeHistoryToDate.setOnClickListener {
                val currentSelectedDate = rechargeHistoryToDateTV.text.toString()
                Utils.showDatePickerDialog(
                    requireContext(),
                    this@RechargeHistoryFragment,
                    "ToDate",
                    currentSelectedDate
                )
            }

            rechargeHistorySwipeRefresh.setOnRefreshListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    rechargeHistorySwipeRefresh.isRefreshing = false
                    Collections.shuffle(rechargeHistoryList, Random(System.currentTimeMillis()))
                    rechargeHistoryAdapter.differ.submitList(rechargeHistoryList)
                }, 2000)

            }
        }

        return binding.root
    }

    private fun makeRechargeHistoryData() {
        rechargeHistoryList = ArrayList<RechargeHistory>()
        rechargeHistoryList.clear()

        rechargeHistoryList.add(
            RechargeHistory(
                "659957",
                "161975",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fairtel.jpeg?alt=media&token=b11e4aca-e1e9-4a8b-89bb-671ab1158252",
                "Airtel",
                "9876543210",
                "47",
                "0.13",
                "40",
                "2024-06-06 15:22:55",
                "SUCCESS",
                "19"
            )
        )

        rechargeHistoryList.add(
            RechargeHistory(
                "629987",
                "156787",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fjio.jpeg?alt=media&token=a009ab77-95f7-4366-868a-38729030dc84",
                "Jio",
                "9876543210",
                "120",
                "23",
                "90",
                "2024-06-05 09:05:11",
                "SUCCESS",
                "61"
            )
        )

        rechargeHistoryList.add(
            RechargeHistory(
                "659957",
                "161975",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fairtel.jpeg?alt=media&token=b11e4aca-e1e9-4a8b-89bb-671ab1158252",
                "Airtel",
                "9876543210",
                "47",
                "0.13",
                "40",
                "2024-06-04 15:22:55",
                "SUCCESS",
                "19"
            )
        )

        rechargeHistoryList.add(
            RechargeHistory(
                "659988",
                "111975",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fjio.jpeg?alt=media&token=a009ab77-95f7-4366-868a-38729030dc84",
                "Jio",
                "9876113210",
                "32",
                "1",
                "11",
                "2024-06-08 19:33:01",
                "SUCCESS",
                "181"
            )
        )

        rechargeHistoryList.add(
            RechargeHistory(
                "669943",
                "191975",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fairtel.jpeg?alt=media&token=b11e4aca-e1e9-4a8b-89bb-671ab1158252",
                "Airtel",
                "8876543210",
                "82",
                "21",
                "40",
                "2024-06-08 11:10:10",
                "SUCCESS",
                "299"
            )
        )

        rechargeHistoryList.add(
            RechargeHistory(
                "669989",
                "191976",
                "https://firebasestorage.googleapis.com/v0/b/satmat-task.appspot.com/o/App_related_icons%2Fairtel.jpeg?alt=media&token=b11e4aca-e1e9-4a8b-89bb-671ab1158252",
                "Airtel",
                "8876543210",
                "82",
                "21",
                "40",
                "2024-06-09 11:10:10",
                "SUCCESS",
                "439"
            )
        )

        rechargeHistoryAdapter.differ.submitList(rechargeHistoryList)
    }

    override fun onDateSelected(date: String, whichDate: String) {
        if (whichDate == "FromDate") {
            binding.rechargeHistoryFromDateTV.text = date
        }
        if (whichDate == "ToDate") {
            binding.rechargeHistoryToDateTV.text = date


            val fromDateString = binding.rechargeHistoryFromDateTV.text.toString() + " 00:00:00"
            val toDateString = binding.rechargeHistoryToDateTV.text.toString() + " 23:59:59"

            val fromDate = Utils.stringToDateSlashDate(fromDateString)
            val toDate = Utils.stringToDateSlashDate(toDateString)

            rechargeHistoryAdapter.filterDataByDate(fromDate, toDate, rechargeHistoryList)
        }

    }

}