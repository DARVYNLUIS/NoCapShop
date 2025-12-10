package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "OrdenCompraEnities")
data class OrdenCompraEntity(
    @PrimaryKey
    val ordenCompraId: Int?,
    val carritoId: Int,
    val usuarioId: Int,
    val pagado: Boolean = false,
    val montoTotal: Double,
    val itbis: Float,
    val fechaCompra: String
)
