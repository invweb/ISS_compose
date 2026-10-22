package com.example.iss.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "iss_positions")
data class IssPositionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
