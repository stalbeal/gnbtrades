package com.saba.gnbtrades.transaction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saba.gnbtrades.R
import com.saba.gnbtrades.databinding.FragmentTransactionsBinding
import com.saba.gnbtrades.transaction.ui.adapter.TransactionAdapter
import com.saba.gnbtrades.transaction.ui.adapter.TransactionClickListener
import com.saba.gnbtrades.transaction.ui.adapter.TransactionItemView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsFragment : Fragment(), TransactionClickListener {
    private val viewModel: TransactionsViewModel by viewModels()
    private lateinit var binding: FragmentTransactionsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(requireActivity()) {
            when {
                it.loading -> showLoading()
                it.viewTransactions != null -> setUpTransactions(it.viewTransactions)
            }
        }

        viewModel.event.observe(requireActivity()) {

            when (val currentEvent = it?.getIfNotHandle()) {
                is TransactionsViewModel.Event.ShowTransactionDetail -> openDetail(currentEvent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onItemClick(transaction: TransactionItemView) {
        viewModel.onItemSelected(transaction.sku)
    }

    private fun openDetail(detail: TransactionsViewModel.Event.ShowTransactionDetail) {

        val bundle = bundleOf("sku" to detail.sku, "transactions" to detail.transactions)
        findNavController().navigate(R.id.transactionDetailFragment, bundle)
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE
        binding.rvTransactionsList.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.loading.visibility = View.GONE
        binding.rvTransactionsList.visibility = View.VISIBLE
    }

    private fun setUpTransactions(transactions: List<TransactionItemView>) {
        hideLoading()
        binding.rvTransactionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactionsList.adapter = TransactionAdapter(transactions, this)
    }
}
