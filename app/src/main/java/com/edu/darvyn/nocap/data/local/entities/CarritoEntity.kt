package com.edu.darvyn.nocap.data.local.entities

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CarritoEntities")
data class CarritoEntity(
    @PrimaryKey
    val carritoId: Int?,
    val usuarioId: Int,
    val montoTotal: Double,
    val fechaCreacion: String,
    val pedido: Boolean
)
