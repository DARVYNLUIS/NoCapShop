package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PagosEntities")
data class PagosEntity (
    @PrimaryKey
    val pagoId: Int,
    val ordenCompraId: Int,
    val usuarioId: Int,
    val montoPagado: Double,
    val fechaPago: String,
    val pagado: Boolean

)