package com.example.crypitomonitor.Service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypitomonitor.Service.CryptoService
import com.example.crypitomonitor.Service.TickerResponse
import com.example.crypitomonitor.Service.Ticker
import com.example.crypitomonitor.state.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CryptoViewModel(
    private val service: CryptoService
) : ViewModel() {

    private val _tickerLiveData = MutableLiveData<ScreenState>()
    val tickerLiveData: LiveData<ScreenState> = _tickerLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetch()
        }
    }

    private suspend fun fetch() {
        _tickerLiveData.postValue(ScreenState.Loading)

        try {
            val response = service.fetchCoinTicker()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    _tickerLiveData.postValue(ScreenState.Success(body.ticker))
                } else {
                    _tickerLiveData.postValue(ScreenState.Error(Exception("Resposta vazia")))
                }
            } else {
                _tickerLiveData.postValue(ScreenState.Error(Exception("Erro: ${response.code()}")))
            }
        } catch (exception: Throwable) {
            _tickerLiveData.postValue(ScreenState.Error(exception))
        }
    }
}