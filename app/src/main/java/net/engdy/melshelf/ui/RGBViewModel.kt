package net.engdy.melshelf.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import net.engdy.melshelf.model.RGBColor

class RGBViewModel(lastAddress: String) : ViewModel() {
    private val _uiState = MutableStateFlow(RGBUiState(ipAddress = lastAddress))
    val uiState: StateFlow<RGBUiState> = _uiState.asStateFlow()

    fun connect(address: String) {
        Log.d(TAG, "connect($address)")
        _uiState.update { currentState ->
            currentState.copy(
                isConnected = true,
                ipAddress = address
            )
        }
    }

    fun setColor(newColor: RGBColor) {
        Log.d(TAG, "setColor($newColor)")
        _uiState.update { currentState ->
            currentState.copy(
                color = newColor
            )
        }
    }

    fun disconnect() {
        Log.d(TAG, "disconnect()")
        _uiState.update { currentState ->
            currentState.copy(
                isConnected = false
            )
        }
    }

    companion object {
        val TAG = RGBViewModel::class.simpleName
    }
}