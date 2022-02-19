package com.saba.gnbtrades.rate.usecase
import com.saba.gnbtrades.rate.models.Rate
import com.saba.gnbtrades.rate.repository.RatesRepository
import javax.inject.Inject

class GetRatesUseCase @Inject constructor(private val ratesRepository: RatesRepository) {

    suspend fun execute(): List<Rate> = ratesRepository.getRates()

}
