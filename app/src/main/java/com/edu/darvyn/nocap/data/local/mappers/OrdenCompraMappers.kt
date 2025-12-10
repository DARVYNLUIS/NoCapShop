package com.edu.darvyn.nocap.data.local.mappers

import com.edu.darvyn.nocap.data.local.entities.OrdenCompraEntity
import com.edu.darvyn.nocap.domain.model.OrdenCompra

fun OrdenCompraEntity.toModel() = OrdenCompra(
    ordenCompraId = ordenCompraId,
    carritoId = carritoId,
    usuarioId = usuarioId,
    pagado = pagado,
    montoTotal = montoTotal,
    itbis = itbis.toDouble(),
    fechaCompra = fechaCompra,
    numeroOrden = "ORD-${ordenCompraId?.toString()?.padStart(8, '0') ?: "00000000"}",
    items = emptyList()
)

fun OrdenCompra.toEntity() = OrdenCompraEntity(
    ordenCompraId = ordenCompraId,
    carritoId = carritoId,
    usuarioId = usuarioId,
    pagado = pagado,
    montoTotal = montoTotal,
    itbis = itbis.toFloat(),
    fechaCompra = fechaCompra
)