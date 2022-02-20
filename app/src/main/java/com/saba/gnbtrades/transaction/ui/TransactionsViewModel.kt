package com.saba.gnbtrades.transaction.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saba.gnbtrades.transaction.adapter.TransactionItemView
import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.transaction.usecase.GetTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(private val getTransactionUseCase: GetTransactionUseCase) :
    ViewModel() {

    private val _state = MutableLiveData(State())

    val state: LiveData<State> = _state

    init {
        getTransactions()
    }

    private fun getTransactions() {
        viewModelScope.launch {
            _state.value = _state.value?.copy(
                loading = true
            )
            val transactions = withContext(Dispatchers.IO) {
                val trans = getTransactionUseCase.execute()
                Pair(trans, trans.map {
                    TransactionItemView(it.key, it.value.size)
                })
            }
            _state.value = _state.value?.copy(
                transactions = transactions.first,
                viewTransactions = transactions.second,
                loading = false
            )
        }
    }

    data class State(
        val transactions: Map<String, List<Transaction>>? = null,
        val loading: Boolean = false,
        val viewTransactions: List<TransactionItemView>? = null
    )
}
