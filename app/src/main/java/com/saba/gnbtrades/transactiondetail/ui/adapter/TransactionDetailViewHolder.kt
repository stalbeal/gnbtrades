package com.saba.gnbtrades.transactiondetail.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.saba.gnbtrades.R
import com.saba.gnbtrades.databinding.ItemDetailTransactionBinding

class TransactionDetailViewHolder(private val binding: ItemDetailTransactionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(transactionValue: String) {
        binding.tvAmount.text = itemView.context.getString(R.string.money, transactionValue.toString())
    }

}
