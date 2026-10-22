package com.example.iss.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iss.data.AstroPerson
import com.example.iss.data.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

data class AstroUiState(
    val people: List<AstroPerson> = emptyList(),
    val totalNumber: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)

class AstroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AstroUiState())
    val uiState: StateFlow<AstroUiState> = _uiState.asStateFlow()

    init {
        fetchAstroPeople()
    }

    fun retryFetch() {
        _uiState.value = AstroUiState(isLoading = true, error = null)
        fetchAstroPeople()
    }

    private fun fetchAstroPeople() {
        viewModelScope.launch {
            var retryDelay = 1000L
            val maxRetries = 3

            for (attempt in 0..maxRetries) {
                try {
                    val response = RetrofitClient.apiService.getAstroPeople()
                    _uiState.value = AstroUiState(
                        people = response.people,
                        totalNumber = response.number,
                        isLoading = false
                    )
                    return@launch
                } catch (e: Exception) {
                    if (attempt < maxRetries) {
                        delay(retryDelay)
                        retryDelay = minOf(retryDelay * 2, 10_000L)
                    } else {
                        _uiState.value = AstroUiState(
                            isLoading = false,
                            error = formatErrorMessage(e)
                        )
                    }
                }
            }
        }
    }

    private fun formatErrorMessage(e: Exception): String = when (e) {
        is SocketTimeoutException -> "Connection timed out. Check your internet connection."
        is UnknownHostException -> "Unable to reach server. Check your internet connection."
        else -> e.message ?: "Unexpected error occurred. Please try again."
    }
}
