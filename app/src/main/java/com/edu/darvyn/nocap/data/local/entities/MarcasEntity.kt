package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "MarcasEntities")
data class MarcasEntity (
    @PrimaryKey
    val marcaId: Int?,
    val nombre: String,
    val activa: Boolean
)