package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EstadosEntities")
data class EstadosEntity (
    @PrimaryKey
    val estadoId: Int?,
    val nombre: String
)