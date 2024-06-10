package com.example.satmattask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.satmattask.databinding.RechargeHistoryItemBinding
import com.example.satmattask.model.reports.RechargeHistory
import com.example.satmattask.utils.Utils
import java.util.Date

class RechargeHistoryAdapter : RecyclerView.Adapter<RechargeHistoryAdapter.holder>() {
    class holder(val binding: RechargeHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: RechargeHistory?) {
            binding.apply {
                if (data != null) {
                    rechargeHistoryItemTXNIDTV.text = data.txnID
                    rechargeHistoryItemNumIDTV.text = data.numID
                    rechargeHistoryItemOperatorTV.text = data.operatorName
                    rechargeHistoryItemPhoneTV.text = data.phoneNum
                    rechargeHistoryOpeningBalanceTV.text = "\u20B9 " + data.openingBalance
                    rechargeHistoryCommissionBalanceTV.text = "\u20B9 " + data.commissionBalance
                    rechargeHistoryClosingBalanceTV.text = "\u20B9 " + data.closingBalance
                    rechargeHistoryDateTV.text = data.balanceDate
                    rechargeHistorySuccessTV.text = data.balanceStatue
                    rechargeHistoryPriceTV.text = "\u20B9 " + data.balancePrice

                    Glide.with(itemView.context).load(data.operatorImage)
                        .into(rechargeHistoryItemOperatorIV)
                }
            }
        }

    }

    val diffUtil = object : DiffUtil.ItemCallback<RechargeHistory>() {
        override fun areItemsTheSame(oldItem: RechargeHistory, newItem: RechargeHistory): Boolean {
            return oldItem.txnID == newItem.txnID
        }

        override fun areContentsTheSame(
            oldItem: RechargeHistory,
            newItem: RechargeHistory
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        return holder(
            RechargeHistoryItemBinding.inflate(
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
        val data = differ.currentList[position]
        holder.bind(data)
    }

    fun filterDataByDate(
        fromDate: Date,
        toDate: Date,
        rechargeHistoryList: ArrayList<RechargeHistory>
    ) {
        val filteredList = rechargeHistoryList.filter { item ->
            val itemDate = Utils.stringToDate(item.balanceDate)
            itemDate in fromDate..toDate
        }
        setData(filteredList.toMutableList())
    }

    fun setData(data: MutableList<RechargeHistory>) {
        differ.submitList(data)
    }

}