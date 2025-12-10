package com.edu.darvyn.nocap.domain.model

data class OrdenCompra(
    val ordenCompraId: Int?,
    val carritoId: Int,
    val usuarioId: Int,
    val pagado: Boolean,
    val montoTotal: Double,
    val itbis: Double,
    val fechaCompra: String,
    val numeroOrden: String,
    val items: List<CarritoItem> = emptyList()
)