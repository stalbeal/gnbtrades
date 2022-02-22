package com.saba.gnbtrades.transactiondetail.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.util.CoroutineDispatchers
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode

class TransactionDetailViewModel @AssistedInject constructor(
    @Assisted private val sku: String,
    @Assisted private val transactions: List<Transaction>,
    private val convertCurrencyTransactionsAmountToEURUseCase: ConvertCurrencyTransactionsAmountToEURUseCase,
    private val coroutineDispatcher: CoroutineDispatchers
) : ViewModel() {

    private val _state = MutableLiveData(State(sku = sku, transactions = transactions))

    val state: LiveData<State> = _state

    init {
        getTransactionsWithAmountsInEUR()
    }

    private fun getTransactionsWithAmountsInEUR() {

        viewModelScope.launch {

            val transactionsResultants = withContext(coroutineDispatcher.io) {
                convertCurrencyTransactionsAmountToEURUseCase.execute(transactions)
            }

            val totalAmount = transactionsResultants.sumOf {
                it.amount
            }

            _state.value =
                _state.value?.copy(
                    transactions = transactionsResultants,
                    loading = false,
                    totalAmount = totalAmount.setScale(
                        2,
                        RoundingMode.HALF_EVEN
                    )
                )
        }
    }

    data class State(
        val loading: Boolean = true,
        val sku: String,
        val transactions: List<Transaction>,
        val totalAmount: BigDecimal? = null
    )

    @AssistedFactory
    interface Factory {
        fun create(sku: String, transactions: List<Transaction>): TransactionDetailViewModel
    }
}


