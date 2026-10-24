package com.example.iss.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iss.data.CustomLandmark
import com.example.iss.data.CustomLandmarkStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomLandmarkViewModel(application: Application) : AndroidViewModel(application) {

    private val _landmarks = MutableStateFlow<List<CustomLandmark>>(emptyList())
    val landmarks: StateFlow<List<CustomLandmark>> = _landmarks.asStateFlow()

    init {
        loadLandmarks()
    }

    private fun loadLandmarks() {
        val context = getApplication<Application>()
        _landmarks.value = CustomLandmarkStorage.loadLandmarks(context)
    }

    fun addLandmark(name: String, latitude: Double, longitude: Double) {
        val context = getApplication<Application>()
        val current = _landmarks.value.toMutableList()
        current.add(CustomLandmark(name = name, latitude = latitude, longitude = longitude))
        CustomLandmarkStorage.saveLandmarks(context, current)
        _landmarks.value = current
    }

    fun deleteLandmark(landmark: CustomLandmark) {
        val context = getApplication<Application>()
        val current = _landmarks.value.toMutableList()
        current.remove(landmark)
        CustomLandmarkStorage.saveLandmarks(context, current)
        _landmarks.value = current
    }

    fun clearAllLandmarks() {
        val context = getApplication<Application>()
        CustomLandmarkStorage.clearLandmarks(context)
        _landmarks.value = emptyList()
    }
}
