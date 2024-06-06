package com.example.satmattask.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.satmattask.databinding.RechargeplansBottomsheetItemBinding
import com.example.satmattask.model.getRechargePlans.Record

class RechargeBottomSheetAdapter(private val selectedRecharge: (String) -> Unit) :
    RecyclerView.Adapter<RechargeBottomSheetAdapter.holder>() {
    class holder(val binding: RechargeplansBottomsheetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Record?, selectedRecharge: (String) -> Unit) {
            binding.apply {
                planPriceTV.text = "\u20B9" + result?.rs.toString()
                planDescriptionTV.text = result?.desc.toString()

                if (result?.desc.toString().equals("Plan Not Available") || result?.desc.toString()
                        .equals("Unable to Plans")
                ) {
                    planPriceTV.visibility = View.GONE
                    planSelectBTN.visibility = View.GONE
                }

                planSelectBTN.setOnClickListener {
                    selectedRecharge(result?.rs.toString())
                }
            }
        }
    }

    val diffUtil = object : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        return holder(
            RechargeplansBottomsheetItemBinding.inflate(
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
        val result = differ.currentList[position]

        holder.bind(result, selectedRecharge)
    }
}