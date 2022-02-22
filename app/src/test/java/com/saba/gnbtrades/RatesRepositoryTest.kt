package com.saba.gnbtrades

import com.saba.gnbtrades.rate.model.Rate
import com.saba.gnbtrades.rate.network.ApiRateResponse
import com.saba.gnbtrades.rate.network.ApiRateService
import com.saba.gnbtrades.rate.repository.RatesRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class RatesRepositoryTest {

    private val service: ApiRateService = mockk()
    private lateinit var repository: RatesRepositoryImpl

    @Before
    fun setup() {
        repository = RatesRepositoryImpl(service)
    }

    @Test
    fun `when getRates called then rates got from backend and api rates mapped to rates`() =
        runTest {

            coEvery { service.getRates() } answers {
                listOf(
                    ApiRateResponse("EUR", "USD", "1.2"),
                    ApiRateResponse("AUD", "USD", "0.7")
                )
            }

            val response = repository.getRates()

            Assert.assertEquals(
                listOf(
                    Rate(Currency.EUR, Currency.USD, BigDecimal.valueOf(1.2)),
                    Rate(Currency.AUD, Currency.USD, BigDecimal.valueOf(.7))
                ),
                response
            )

            coVerify(exactly = 1) {
                service.getRates()
            }
        }

    @Test(expected = IllegalArgumentException::class)
    fun `when getRates called and unsupported currency return then it should crash`() = runTest {
        coEvery { service.getRates() } answers {
            listOf(
                ApiRateResponse("COP", "USD", "1.2")
            )
        }

        repository.getRates()
    }
}
