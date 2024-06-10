package com.example.satmattask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.satmattask.databinding.LedgerReportRvItemBinding
import com.example.satmattask.model.reports.LedgerData
import com.example.satmattask.model.reports.RechargeHistory
import com.example.satmattask.utils.Utils
import java.util.Date

class LedgerReportAdapter : RecyclerView.Adapter<LedgerReportAdapter.holder>() {

    class holder(val binding: LedgerReportRvItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<LedgerData>() {
        override fun areItemsTheSame(oldItem: LedgerData, newItem: LedgerData): Boolean {
            return oldItem.txnID == newItem.txnID
        }

        override fun areContentsTheSame(oldItem: LedgerData, newItem: LedgerData): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        return holder(
            LedgerReportRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        val ledgerItem = differ.currentList[position]

        holder.binding.apply {
            ledgerReportTxnID.text = ledgerItem.txnID
            ledgerReportDate.text = ledgerItem.ledgerDate
            ledgerReportTransactionType.text = "Transaction type : ${ledgerItem.transactionType}"
            ledgerReportItemOpeningBalanceTV.text = "\u20B9 ${ledgerItem.openingBalance}"
            ledgerReportItemBalanceCreditedTV.text = "\u20B9 ${ledgerItem.creditedBalance}"
            ledgerReportItemBalanceDebitedTV.text = "\u20B9 ${ledgerItem.debitedBalance}"
            ledgerReportItemClosingBalanceTV.text = "\u20B9 ${ledgerItem.closingBalance}"
        }
    }

    fun filterDataByDate(
        fromDate: Date,
        toDate: Date,
        ledgerReportList: ArrayList<LedgerData>
    ) {
        val filteredList = ledgerReportList.filter { item ->
            val itemDate = Utils.stringToDate(item.ledgerDate)
            itemDate in fromDate..toDate
        }
        setData(filteredList.toMutableList())
    }

    fun setData(data: MutableList<LedgerData>) {
        differ.submitList(data)
    }
}