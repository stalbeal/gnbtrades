package com.saba.gnbtrades

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.transactiondetail.ui.TransactionDetailViewModel
import com.saba.gnbtrades.transactiondetail.usecase.ConvertCurrencyTransactionsAmountToEURUseCase
import com.saba.gnbtrades.util.CoroutineDispatchers
import com.saba.gnbtrades.util.getFormattedAmount
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class TransactionDetailViewModelTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val mockTransactions = listOf(
        Transaction("001", BigDecimal.valueOf(3), Currency.AUD),
        Transaction("001", BigDecimal.valueOf(4), Currency.EUR),
        Transaction("001", BigDecimal.valueOf(5), Currency.USD)
    )

    private val mockTransactionsInEUR = listOf(
        Transaction("001", BigDecimal.valueOf(3.40).getFormattedAmount(), Currency.EUR),
        Transaction("001", BigDecimal.valueOf(4), Currency.EUR),
        Transaction("001", BigDecimal.valueOf(4.50).getFormattedAmount(), Currency.EUR)
    )

    private val convertCurrencyTransactionsAmountToEURUseCase: ConvertCurrencyTransactionsAmountToEURUseCase =
        mockk()

    private lateinit var viewModel: TransactionDetailViewModel

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        coEvery {
            convertCurrencyTransactionsAmountToEURUseCase.execute(mockTransactions)
        } answers { mockTransactionsInEUR }


    }

    @Test
    fun `when viewmodel created it should get transaction amount in EUR`() = runTest {

        viewModel = TransactionDetailViewModel(
            "001",
            mockTransactions,
            convertCurrencyTransactionsAmountToEURUseCase,
            CoroutineDispatchers(dispatcher, dispatcher)
        )
        var numberOfStatesEmitted = 1
        viewModel.state.observeForever {
            if (numberOfStatesEmitted == 1) {
                Assert.assertEquals(
                    TransactionDetailViewModel.State(
                        loading = true,
                        sku = "001",
                        transactions = mockTransactions
                    ),
                    it
                )
            } else {
                Assert.assertEquals(
                    TransactionDetailViewModel.State(
                        loading = false,
                        sku = "001",
                        transactions = mockTransactionsInEUR,
                        totalAmount = BigDecimal(11.90).getFormattedAmount()
                    ),
                    it
                )
            }
            numberOfStatesEmitted++
        }

        Assert.assertEquals(2, numberOfStatesEmitted)
    }
}
