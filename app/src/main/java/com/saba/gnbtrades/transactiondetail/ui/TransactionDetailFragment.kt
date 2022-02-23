package com.saba.gnbtrades.transactiondetail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.saba.gnbtrades.R
import com.saba.gnbtrades.databinding.FragmentTransactionDetailBinding
import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.transactiondetail.ui.adapter.TransactionDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import javax.inject.Inject

@AndroidEntryPoint
class TransactionDetailFragment : Fragment() {

    @Inject
    lateinit var factory: TransactionDetailViewModel.Factory
    private lateinit var binding: FragmentTransactionDetailBinding

    private val viewModel: TransactionDetailViewModel by viewModels {
        ViewModelFactory.from {
            factory.create(
                requireArguments().getString("sku")!!,
                requireArguments().getParcelableArrayList("transactions")!!
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()
    }

    private fun observeViewModelState() {
        viewModel.state.observe(requireActivity()) {
            when {
                it.loading -> showLoading()
                it.totalAmount != null -> setUpView(
                    it.sku,
                    it.transactions,
                    it.totalAmount
                )

            }
        }
    }

    private fun setUpView(sku: String, transactions: List<Transaction>, totalAmount: BigDecimal) {
        hideLoading()
        setUpTitle(sku)
        setUpTransactionsList(transactions)
        setUpTotalAmount(totalAmount)
    }

    private fun setUpTitle(sku: String) {
        binding.tvTransactionIdentifier.text = sku
    }

    private fun setUpTotalAmount(totalAmount: BigDecimal) {
        binding.tvTotalAmount.text =
            getString(R.string.transaction_detail_total_amount_title, totalAmount.toString())
    }

    private fun setUpTransactionsList(transactions: List<Transaction>) {
        binding.rvTransactionsList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactionsList.adapter =
            TransactionDetailAdapter(transactions.map { it.amount.toString() })
    }

    private fun showLoading() {
        binding.loading.visibility = View.VISIBLE

        showViews()
    }

    private fun showViews() {
        binding.tvTotalAmount.visibility = View.GONE
        binding.tvTransactionIdentifier.visibility = View.GONE
        binding.separator.visibility = View.GONE
        binding.rvTransactionsList.visibility = View.GONE
        binding.tvSkuLabel.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.loading.visibility = View.GONE

        hideViews()
    }

    private fun hideViews() {
        binding.tvTotalAmount.visibility = View.VISIBLE
        binding.tvTransactionIdentifier.visibility = View.VISIBLE
        binding.separator.visibility = View.VISIBLE
        binding.rvTransactionsList.visibility = View.VISIBLE
        binding.tvSkuLabel.visibility = View.VISIBLE

    }
}
