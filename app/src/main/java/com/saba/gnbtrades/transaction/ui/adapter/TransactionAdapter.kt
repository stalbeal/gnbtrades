package com.saba.gnbtrades.transaction.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saba.gnbtrades.databinding.ItemTransactionBinding

class TransactionAdapter(
    private val transactions: List<TransactionItemView>,
    private val transactionListener: TransactionClickListener
) :
    RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position], transactionListener)
    }

    override fun getItemCount(): Int = transactions.size
}

