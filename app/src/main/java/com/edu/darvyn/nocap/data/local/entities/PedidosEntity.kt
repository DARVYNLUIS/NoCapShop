package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "PedidoEntities")
data class PedidosEntity(
    @PrimaryKey(autoGenerate = true)
    val pedidoId: Int = 0,
    val ordenCompraId: Int,
    val usuarioId: Int,
    val estado: String = "Pendiente", // Pendiente, En Proceso, Enviado, Entregado, Cancelado
    val fechaCreacion: String = obtenerFechaActual(),
    val fechaEntregaEstimada: String = fechaEntregaAleatoria()
)

fun obtenerFechaActual(): String {
    return LocalDate.now().toString() // formato YYYY-MM-DD
}

fun fechaEntregaAleatoria(): String {
    val dias = (3..10).random() // aleatorio entre 3 y 10 días
    return LocalDate.now().plusDays(dias.toLong()).toString()
}