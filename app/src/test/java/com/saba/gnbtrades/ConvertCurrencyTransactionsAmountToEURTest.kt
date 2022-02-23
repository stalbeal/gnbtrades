package com.saba.gnbtrades

import com.saba.gnbtrades.transaction.model.Transaction
import com.saba.gnbtrades.transactiondetail.model.Rate
import com.saba.gnbtrades.transactiondetail.usecase.ConvertCurrencyTransactionsAmountToEURUseCase
import com.saba.gnbtrades.transactiondetail.usecase.GetRatesUseCase
import com.saba.gnbtrades.util.getFormattedAmount
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class ConvertCurrencyTransactionsAmountToEURTest {

    private val getRatesUseCase: GetRatesUseCase = mockk()

    private lateinit var usecase: ConvertCurrencyTransactionsAmountToEURUseCase

    private val mockTransactions = listOf(
        Transaction("001", BigDecimal.valueOf(3), Currency.AUD),
        Transaction("002", BigDecimal.valueOf(4), Currency.EUR),
        Transaction("003", BigDecimal.valueOf(5), Currency.USD)
    )

    @Before
    fun setup() {
        coEvery {
            getRatesUseCase.execute()
        } answers {
            mapOf(
                Currency.AUD to listOf(
                    Rate(
                        from = Currency.AUD,
                        to = Currency.CAD,
                        value = BigDecimal.valueOf(.7)
                    )
                ),
                Currency.CAD to listOf(
                    Rate(
                        from = Currency.CAD,
                        to = Currency.AUD,
                        value = BigDecimal.valueOf(1.2)
                    ),
                    Rate(
                        from = Currency.CAD,
                        to = Currency.USD,
                        value = BigDecimal.valueOf(1.8)
                    )
                ),
                Currency.USD to listOf(
                    Rate(
                        from = Currency.USD,
                        to = Currency.EUR,
                        value = BigDecimal.valueOf(.9)
                    ),
                    Rate(
                        from = Currency.USD,
                        to = Currency.CAD,
                        value = BigDecimal.valueOf(1.3)
                    )
                )
            )
        }
        usecase = ConvertCurrencyTransactionsAmountToEURUseCase(getRatesUseCase)
    }

    @Test
    fun `given a transaction list with transaction not in EUR when executed then it should convert transaction amounts to eur`() =
        runTest {
            val response = usecase.execute(mockTransactions)

            Assert.assertEquals(
                listOf(
                    Transaction("001", BigDecimal.valueOf(3.40).getFormattedAmount(), Currency.EUR),
                    Transaction("002", BigDecimal.valueOf(4), Currency.EUR),
                    Transaction("003", BigDecimal.valueOf(4.50).getFormattedAmount(), Currency.EUR)
                ),
                response
            )

            coVerify(exactly = 1) {
                getRatesUseCase.execute()
            }
        }

    @Test
    fun `given rates not containing conversions from one transaction when executed then it should crash`() =
        runTest {
            coEvery {
                getRatesUseCase.execute()
            } answers {
                mapOf(
                    Currency.AUD to listOf(
                        Rate(
                            from = Currency.AUD,
                            to = Currency.CAD,
                            value = BigDecimal.valueOf(.7)
                        )
                    ),
                    Currency.CAD to listOf(),
                    Currency.USD to listOf(
                        Rate(
                            from = Currency.USD,
                            to = Currency.EUR,
                            value = BigDecimal.valueOf(.9)
                        ),
                        Rate(
                            from = Currency.USD,
                            to = Currency.CAD,
                            value = BigDecimal.valueOf(1.3)
                        )
                    )
                )
            }

            try {
                usecase.execute(mockTransactions)
            } catch (e: Exception) {
                assert(e is IllegalStateException)
                Assert.assertEquals(
                    "There are not routes for conversion from AUD to EUR",
                    e.message
                )
            } finally {
                coVerify(exactly = 1) {
                    getRatesUseCase.execute()
                }
            }
        }

    @Test
    fun `given rates leading to dead end when executed then it should crash`() =
        runTest {
            coEvery {
                getRatesUseCase.execute()
            } answers {
                mapOf(
                    Currency.AUD to listOf(
                        Rate(
                            from = Currency.AUD,
                            to = Currency.CAD,
                            value = BigDecimal.valueOf(.7)
                        )
                    ),
                    Currency.USD to listOf(
                        Rate(
                            from = Currency.USD,
                            to = Currency.EUR,
                            value = BigDecimal.valueOf(.9)
                        ),
                        Rate(
                            from = Currency.USD,
                            to = Currency.CAD,
                            value = BigDecimal.valueOf(1.3)
                        )
                    )
                )
            }

            try {
                usecase.execute(mockTransactions)
            } catch (e: Exception) {
                assert(e is IllegalStateException)
                Assert.assertEquals(
                    "There are not routes for conversion from AUD to EUR",
                    e.message
                )
            } finally {
                coVerify(exactly = 1) {
                    getRatesUseCase.execute()
                }
            }
        }

}
