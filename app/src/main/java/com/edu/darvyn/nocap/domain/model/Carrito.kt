package com.edu.darvyn.nocap.domain.model

data class Carrito (
val carritoId: Int?,
val usuarioId: Int,
val montoTotal: Double,
val fechaCreacion: String,
val items: List<CarritoItem> = emptyList(),
val pedido: Boolean = false
)