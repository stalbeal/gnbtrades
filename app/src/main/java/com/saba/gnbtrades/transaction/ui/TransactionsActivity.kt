package com.saba.gnbtrades.transaction.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.saba.gnbtrades.databinding.ActivityTransactionsBinding
import com.saba.gnbtrades.transaction.adapter.TransactionAdapter
import com.saba.gnbtrades.transaction.adapter.TransactionItemView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsActivity : AppCompatActivity() {

    private val viewModel: TransactionsViewModel by viewModels()
    private lateinit var binding: ActivityTransactionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.state.observe(this) {
            when {
                it.loading -> showLoading()
                it.viewTransactions != null -> setUpTransactions(it.viewTransactions)
            }
        }
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
        binding.rvTransactionsList.visibility = View.GONE
        binding.tvTitle.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.loading.visibility = View.GONE
        binding.rvTransactionsList.visibility = View.VISIBLE
        binding.tvTitle.visibility = View.VISIBLE
    }

    private fun setUpTransactions(transactions: List<TransactionItemView>) {
        hideLoading()
        binding.rvTransactionsList.layoutManager = LinearLayoutManager(this)
        binding.rvTransactionsList.adapter = TransactionAdapter(transactions)
    }
}

