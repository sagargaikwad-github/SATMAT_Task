package com.example.satmattask.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.satmattask.R
import com.example.satmattask.databinding.BillPaymentsItemBinding
import com.example.satmattask.model.BillPaymentsService

class BillPaymentsAdapter : RecyclerView.Adapter<BillPaymentsAdapter.ViewHolder>() {
    class ViewHolder(val binding: BillPaymentsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(paymentService: BillPaymentsService?) {
            binding.fastServicesTV.text = paymentService?.ServiceName
            Glide.with(itemView.context).load(paymentService?.ServiceImage)
                .into(binding.fastServicesIV)

            binding.billPaymentsBG.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    itemView.context,
                    paymentService?.ServiceBgColor!!
                )
            )
        }
    }

    val diffUtil = object : DiffUtil.ItemCallback<BillPaymentsService>() {
        override fun areItemsTheSame(
            oldItem: BillPaymentsService,
            newItem: BillPaymentsService
        ): Boolean {
            return oldItem.ServiceId == newItem.ServiceId
        }

        override fun areContentsTheSame(
            oldItem: BillPaymentsService,
            newItem: BillPaymentsService
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BillPaymentsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paymentService = differ.currentList[position]

        holder.bind(paymentService)
    }


}