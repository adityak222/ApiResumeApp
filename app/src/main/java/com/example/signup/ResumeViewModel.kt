package com.example.signup

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signup.Resume
import com.example.signup.RetrofitInstance
import com.example.signup.LocationService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// UI State for the resume data
sealed interface ResumeUiState {
    object Loading : ResumeUiState
    data class Success(val resume: Resume) : ResumeUiState
    data class Error(val message: String) : ResumeUiState
}

class ResumeViewModel(private val locationService: LocationService) : ViewModel() {

    private val _resumeState = MutableStateFlow<ResumeUiState>(ResumeUiState.Loading)
    val resumeState: StateFlow<ResumeUiState> = _resumeState.asStateFlow()

    private val _location = MutableStateFlow<Pair<Double, Double>?>(null)
    val location: StateFlow<Pair<Double, Double>?> = _location.asStateFlow()

    private val _fontSize = MutableStateFlow(18f)
    val fontSize = _fontSize.asStateFlow()

    private val _fontColor = MutableStateFlow(Color.Black)
    val fontColor = _fontColor.asStateFlow()

    private val _backgroundColor = MutableStateFlow(Color(0xFFE0F7FA))
    val backgroundColor = _backgroundColor.asStateFlow()

    init {
       fetchResume("ADITYA KASOUDHAN") //Name is hardcoded as per requirement
    }

    fun fetchResume(name: String) {
        viewModelScope.launch {
            _resumeState.value = ResumeUiState.Loading
            try {
                val response = RetrofitInstance.api.getResume(name)
                if (response.isSuccessful && response.body() != null) {
                    _resumeState.value = ResumeUiState.Success(response.body()!!)
                } else {
                    _resumeState.value = ResumeUiState.Error("Failed to fetch data")
                }
            } catch (e: Exception) {
                _resumeState.value = ResumeUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun startLocationUpdates() {
        viewModelScope.launch {
            locationService.requestLocationUpdates().collect {
                _location.value = it
            }
        }
    }

    fun onFontSizeChange(size: Float) {
        _fontSize.value = size
    }

    fun onFontColorChange(color: Color) {
        _fontColor.value = color
    }

    fun onBackgroundColorChange(color: Color) {
        _backgroundColor.value = color
    }
}

// Simple ViewModel Factory to pass LocationService
class ResumeViewModelFactory(private val locationService: LocationService) :
    androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResumeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResumeViewModel(locationService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}