package com.example.satmattask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.satmattask.R
import com.example.satmattask.databinding.AllservicesItemBinding
import com.example.satmattask.databinding.FastservicesItemBinding
import com.example.satmattask.model.AllServices
import kotlin.reflect.KFunction2

class AllServicesAdapter(val itemClick: KFunction2<String, String, Unit>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        companion object{
            const val FIRST_VIEW_TYPE = 1
            const val SECOND_VIEW_TYPE = 2
        }
    class FastServiceHolder(val binding: FastservicesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: AllServices) {
            when (user.userImage) {
                "topup.jpg" -> binding.fastServicesIV.setImageResource(R.drawable.ic_topup)
                "payout.jpg" -> binding.fastServicesIV.setImageResource(R.drawable.ic_payout)
                "myqr.jpg" -> binding.fastServicesIV.setImageResource(R.drawable.ic_myqr)
                "activateservice.jpg" -> binding.fastServicesIV.setImageResource(R.drawable.ic_activateservices)
            }
            binding.fastServicesTV.text = user.serviceName
        }
    }

    class AllServiceHolder(val binding: AllservicesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: AllServices) {
            Glide.with(itemView.context).load(user.userImage).into(binding.fastServicesIV)
            binding.fastServicesTV.text = user.serviceName
        }
    }


    val diffUtil = object : DiffUtil.ItemCallback<AllServices>() {
        override fun areItemsTheSame(oldItem: AllServices, newItem: AllServices): Boolean {
            return oldItem.serviceId == newItem.serviceId
        }

        override fun areContentsTheSame(oldItem: AllServices, newItem: AllServices): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == FIRST_VIEW_TYPE) {
            return FastServiceHolder(
                FastservicesItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        return AllServiceHolder(
            AllservicesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = differ.currentList[position]


        if (user.viewType === FIRST_VIEW_TYPE) {
            (holder as FastServiceHolder).bind(user)
        } else {
            (holder as AllServiceHolder).bind(user)
        }

        holder.itemView.setOnClickListener {
            itemClick(user.serviceId!!,user.serviceName!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].viewType!!
    }
}