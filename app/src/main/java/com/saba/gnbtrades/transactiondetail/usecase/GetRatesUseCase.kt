package com.saba.gnbtrades.transactiondetail.usecase

import com.saba.gnbtrades.Currency
import com.saba.gnbtrades.transactiondetail.model.Rate
import com.saba.gnbtrades.transactiondetail.repository.RatesRepository
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(private val ratesRepository: RatesRepository) {

    //suspend fun execute(): List<Rate> = ratesRepository.getRates()

    suspend fun execute(): Map<Currency, List<Rate>> {
        val ratesGroupedByFrom = HashMap<Currency, List<Rate>>()

        ratesRepository.getRates().forEach {

            if (!ratesGroupedByFrom.containsKey(it.from)) {
                ratesGroupedByFrom[it.from] = listOf()
            }

            ratesGroupedByFrom[it.from] = ratesGroupedByFrom[it.from]!!.toMutableList().apply {
                add(it)
            }

        }
        return ratesGroupedByFrom
    }
}
