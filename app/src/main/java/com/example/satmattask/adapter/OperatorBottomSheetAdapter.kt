package com.example.satmattask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.satmattask.databinding.OperatorBottomsheetItemBinding
import com.example.satmattask.model.getOperators.Result
import com.example.satmattask.utils.Utils

class OperatorBottomSheetAdapter(val sendResult: (Result) -> Unit) : RecyclerView.Adapter<OperatorBottomSheetAdapter.ViewHolder>() {
    class ViewHolder(val binding: OperatorBottomsheetItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(operator: Result?) {
            binding.operatorName.text = operator?.operatorname
            Glide.with(itemView.context).load(Utils.BASE_URL + operator?.operator_image)
                .into(binding.operatorIV)
        }
    }


    val diffUtil = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.opid == newItem.opid
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OperatorBottomsheetItemBinding.inflate(
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
        val operator = differ.currentList[position]

        holder.bind(operator)
        holder.itemView.setOnClickListener {
            sendResult(operator)
        }
    }

}