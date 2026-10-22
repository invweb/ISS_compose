package com.example.iss.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

object AstroCache {
    private const val KEY_ASTROS = "astro_cache"
    private const val KEY_TIMESTAMP = "astro_cache_timestamp"
    private const val CACHE_MAX_AGE_MS = 5 * 60 * 1000L // 5 minutes

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        prefs = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
    }

    fun isCacheValid(): Boolean {
        val timestamp = prefs.getLong(KEY_TIMESTAMP, 0)
        return System.currentTimeMillis() - timestamp < CACHE_MAX_AGE_MS
    }

    suspend fun getCachedAstros(): List<AstroPerson>? = withContext(Dispatchers.IO) {
        val json = prefs.getString(KEY_ASTROS, null) ?: return@withContext null
        val type = object : TypeToken<List<AstroPerson>>() {}.type
        return@withContext gson.fromJson(json, type)
    }

    suspend fun cacheAstros(people: List<AstroPerson>, number: Int) = withContext(Dispatchers.IO) {
        val json = gson.toJson(people)
        prefs.edit()
            .putString(KEY_ASTROS, json)
            .putLong(KEY_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }

    fun clear() {
        prefs.edit().remove(KEY_ASTROS).remove(KEY_TIMESTAMP).apply()
    }

    fun getParsedTimestamp(): Long {
        return prefs.getLong(KEY_TIMESTAMP, 0)
    }
}
