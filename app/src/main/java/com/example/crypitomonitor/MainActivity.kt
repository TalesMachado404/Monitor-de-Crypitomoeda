package com.example.crypitomonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.crypitomonitor.state.ScreenState
import com.example.crypitomonitor.service.CryptoViewModel
import com.example.crypitomonitor.service.CryptoViewModelFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val viewModel: CryptoViewModel by viewModels { CryptoViewModelFactory() }

    private val textViewBitcoin: TextView by lazy { findViewById(R.id.textViewBitcoin) }
    private val textViewDate: TextView by lazy { findViewById(R.id.textViewDate) }
    private val buttonRefresh: Button by lazy { findViewById(R.id.buttonRefresh) }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progressBar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.tickerLiveData.observe(this) { state ->
            when (state) {
                is ScreenState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    buttonRefresh.visibility = View.GONE
                }
                is ScreenState.Success -> {
                    progressBar.visibility = View.GONE
                    buttonRefresh.visibility = View.VISIBLE
                    textViewBitcoin.text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(
                        state.data.last.toBigDecimal()
                    )
                    textViewDate.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                }
                is ScreenState.Error -> {
                    progressBar.visibility = View.GONE
                    buttonRefresh.visibility = View.VISIBLE
                    Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_LONG).show()
                }
            }
        }

        // ✅ Corrigido: sem viewModelScope aqui
        buttonRefresh.setOnClickListener {
            viewModel.refresh()
        }
    }
}
