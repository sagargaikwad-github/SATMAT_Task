package com.example.satmattask.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.satmattask.Interface.OnDateSelectedListener
import com.example.satmattask.R
import com.example.satmattask.adapter.LedgerReportAdapter
import com.example.satmattask.databinding.ActivityTopUpBinding
import com.example.satmattask.databinding.FragmentLedgerReportBinding
import com.example.satmattask.model.reports.LedgerData
import com.example.satmattask.utils.Utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LedgerReportFragment : Fragment(), OnDateSelectedListener {
    private lateinit var binding: FragmentLedgerReportBinding
    private lateinit var ledgerReportAdapter: LedgerReportAdapter
    private lateinit var ledgerList: ArrayList<LedgerData>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.otherReportsToolbar)
        toolbar.title = "Ledger Report"

        toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLedgerReportBinding.inflate(layoutInflater)

        prepareAdapter()
        getLedgerReports()

        dateSelection()
        searchDataViaDate()
        return binding.root
    }

    private fun searchDataViaDate() {
        binding.ledgerReportGoBTN.setOnClickListener {
            val fromDateString = binding.ledgerReportFromDateTV.text.toString() + " 00:00:00"
            val toDateString = binding.ledgerReportToDateTV.text.toString() + " 23:59:59"

            val fromDate = Utils.stringToDateSlashDate(fromDateString)
            val toDate = Utils.stringToDateSlashDate(toDateString)

            ledgerReportAdapter.filterDataByDate(fromDate, toDate, ledgerList)
        }
    }

    private fun dateSelection() {
        binding.ledgerReportFromDateTV.text =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                Date()
            )

        binding.ledgerReportToDateTV.text =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                Date()
            )

        binding.apply {
            ledgerReportFromDateTV.setOnClickListener {
                val currentSelectedFromDate = ledgerReportFromDateTV.text.toString()
                Utils.showDatePickerDialog(
                    requireContext(),
                    this@LedgerReportFragment,
                    "FromDate",
                    currentSelectedFromDate
                )
            }

            ledgerReportToDateTV.setOnClickListener {
                val currentSelectedToDate = ledgerReportToDateTV.text.toString()
                Utils.showDatePickerDialog(
                    requireContext(),
                    this@LedgerReportFragment,
                    "ToDate",
                    currentSelectedToDate
                )
            }
        }
    }

    private fun getLedgerReports() {
        ledgerList = ArrayList<LedgerData>()
        ledgerList.clear()

        ledgerList.add(LedgerData("54321", "2024-06-07 10:12:54", "UPI", "120", "21", "10", "82"))
        ledgerList.add(
            LedgerData(
                "43210",
                "2024-05-27 14:34:20",
                "Direct Credit",
                "200",
                "150",
                "50",
                "0"
            )
        )
        ledgerList.add(LedgerData("32100", "2024-05-15 19:10:10", "UPI", "299", "0", "14", "251"))
        ledgerList.add(
            LedgerData(
                "21543",
                "2024-04-01 12:09:54",
                "Direct Credit",
                "40",
                "5",
                "3",
                "8"
            )
        )

        ledgerReportAdapter.differ.submitList(ledgerList)
    }

    private fun prepareAdapter() {
        binding.ledgerReportRV.apply {
            ledgerReportAdapter = LedgerReportAdapter()
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ledgerReportAdapter
        }
    }

    override fun onDateSelected(date: String, whichDate: String) {
        when (whichDate) {
            "FromDate" ->
                binding.ledgerReportFromDateTV.text = date

            "ToDate" ->
                binding.ledgerReportToDateTV.text = date
        }
    }
}