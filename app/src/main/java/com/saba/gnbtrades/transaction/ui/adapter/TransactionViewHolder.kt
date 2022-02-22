package com.saba.gnbtrades.transaction.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.saba.gnbtrades.databinding.ItemTransactionBinding

class TransactionViewHolder(private val binding: ItemTransactionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(transaction: TransactionItemView, transactionListener: TransactionClickListener) {
        setUpViews(transaction)

        binding.cvContainer.setOnClickListener {
            transactionListener.onItemClick(transaction)
        }
    }

    private fun setUpViews(transaction: TransactionItemView) {
        binding.tvTransactionIdentifier.text = transaction.sku
        binding.tvTransactionAmount.text = transaction.transactionsQuantity.toString()
    }

}
