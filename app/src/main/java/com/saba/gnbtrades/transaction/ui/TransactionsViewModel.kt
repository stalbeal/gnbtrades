package com.saba.gnbtrades.transaction.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.transaction.ui.adapter.TransactionItemView
import com.saba.gnbtrades.transaction.usecase.GetTransactionUseCase
import com.saba.gnbtrades.util.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactionUseCase: GetTransactionUseCase,
    private val coroutineDispatcher: CoroutineDispatchers
) :
    ViewModel() {

    private val _state = MutableLiveData(State())

    val state: LiveData<State> = _state

    private val _event = MutableLiveData<Event?>(null)

    val event: LiveData<Event?> = _event

    init {
        getTransactions()
    }

    private fun getTransactions() {
        viewModelScope.launch {
            _state.value = _state.value?.copy(
                loading = true
            )
            val transactions = withContext(coroutineDispatcher.io) {
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

    fun onItemSelected(sku: String) {
        val transactions = _state.value?.transactions?.get(sku) ?: throw RuntimeException()

        _event.value = Event.ShowTransactionDetail(transactions, sku)

    }

    data class State(
        val transactions: Map<String, List<Transaction>>? = null,
        val loading: Boolean = false,
        val viewTransactions: List<TransactionItemView>? = null
    )

    sealed class Event {

        private var isHandle: Boolean = false


        fun getIfNotHandle(): Event? {
            if (isHandle) {
                return null
            }

            isHandle = true
            return this
        }


        data class ShowTransactionDetail(val transactions: List<Transaction>, val sku: String) :
            Event()
    }
}
