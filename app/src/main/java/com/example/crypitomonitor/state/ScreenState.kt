package com.example.crypitomonitor.state

import com.example.crypitomonitor.Service.Ticker

sealed class ScreenState {
    object Loading : ScreenState()
    data class Success(val data: Ticker) : ScreenState()
    data class Error(val exception: Throwable) : ScreenState()
}