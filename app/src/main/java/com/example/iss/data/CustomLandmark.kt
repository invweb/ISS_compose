package com.example.iss.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class CustomLandmark(
    val name: String,
    val latitude: Double,
    val longitude: Double
)

object CustomLandmarkStorage {
    private const val KEY_CUSTOM_LANDMARKS = "custom_landmarks"
    private const val PREFS_NAME = "iss_settings"

    private val gson = Gson()

    fun saveLandmarks(context: Context, landmarks: List<CustomLandmark>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(landmarks)
        prefs.edit().putString(KEY_CUSTOM_LANDMARKS, json).apply()
    }

    fun loadLandmarks(context: Context): List<CustomLandmark> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_CUSTOM_LANDMARKS, null) ?: return emptyList()
        val type = object : TypeToken<List<CustomLandmark>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun clearLandmarks(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_CUSTOM_LANDMARKS).apply()
    }
}
