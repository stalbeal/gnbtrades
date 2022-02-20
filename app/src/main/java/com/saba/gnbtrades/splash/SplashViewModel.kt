package com.saba.gnbtrades.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saba.gnbtrades.rate.models.Rate
import com.saba.gnbtrades.rate.usecase.GetRatesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val getRatesUseCase: GetRatesUseCase) : ViewModel() {

    private val _state = MutableLiveData(State())

    val state: LiveData<State> = _state

    init {
        getRates()
    }

    private fun getRates() {
        viewModelScope.launch {
            _state.value = _state.value?.copy(
                loading = true
            )

            val rates = withContext(Dispatchers.IO) {
                getRatesUseCase.execute()
            }

            _state.value = _state.value?.copy(
                loading = false,
                rates = rates
            )
        }
    }

    data class State(
        val loading: Boolean = false,
        val rates: List<Rate>? = null
    )
}
