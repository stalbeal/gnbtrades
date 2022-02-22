package com.saba.gnbtrades.transactiondetail.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saba.gnbtrades.databinding.ItemDetailTransactionBinding

class TransactionDetailAdapter(private val transactionsValues: List<String>) :
    RecyclerView.Adapter<TransactionDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionDetailViewHolder {
        val binding = ItemDetailTransactionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return TransactionDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionDetailViewHolder, position: Int) {
        holder.bind(transactionsValues[position])
    }

    override fun getItemCount(): Int = transactionsValues.size
}

