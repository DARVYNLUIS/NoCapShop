package com.edu.darvyn.nocap.data.local.mappers

import com.edu.darvyn.nocap.data.local.entities.PedidosEntity
import com.edu.darvyn.nocap.domain.model.EstadoPedido
import com.edu.darvyn.nocap.domain.model.Pedido

fun PedidosEntity.toModel(): Pedido {
    return Pedido(
        pedidoId = pedidoId!!,
        ordenCompraId = ordenCompraId,
        usuarioId = usuarioId,
        estado = EstadoPedido.fromString(estado),
        fechaCreacion = fechaCreacion,
        fechaEntregaEstimada = fechaEntregaEstimada
    )
}

fun Pedido.toEntity(): PedidosEntity {
    return PedidosEntity(
        pedidoId = pedidoId,
        ordenCompraId = ordenCompraId,
        usuarioId = usuarioId,
        estado = estado.estado,
        fechaCreacion = fechaCreacion,
        fechaEntregaEstimada = fechaEntregaEstimada
    )
}