package com.example.crypitomonitor.Service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptomonitor.service.CryptoService
import com.example.cryptomonitor.service.TickerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CryptoViewModel(
    private val service: CryptoService
) : ViewModel() {

    private val _tickerLiveData = MutableLiveData<Ticker>()
    val tickerLiveData: LiveData<Ticker> = _tickerLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetch()
        }
    }

    private suspend fun fetch() {
        val response = service.fetchCoinTicker()
        _tickerLiveData.postValue(response.ticker)
    }
}