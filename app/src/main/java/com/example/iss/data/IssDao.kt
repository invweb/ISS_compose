package com.example.iss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IssDao {
    @Insert
    suspend fun insertPosition(position: IssPositionEntity)

    @Query("SELECT * FROM iss_positions ORDER BY timestamp DESC LIMIT 200")
    suspend fun getLastPositions(): List<IssPositionEntity>

    @Query("DELETE FROM iss_positions")
    suspend fun clearAll()

    @Query("SELECT latitude, longitude, timestamp FROM iss_positions ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastPositionRaw(): LastPositionRaw?
}

data class LastPositionRaw(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
