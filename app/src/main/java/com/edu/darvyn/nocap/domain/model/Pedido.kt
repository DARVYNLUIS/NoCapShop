package com.edu.darvyn.nocap.domain.model


data class Pedido(
    val pedidoId: Int,
    val ordenCompraId: Int,
    val usuarioId: Int,
    val estado: EstadoPedido,
    val fechaCreacion: String,
    val fechaEntregaEstimada: String,
    val ordenCompra: OrdenCompra? = null // Opcional para mostrar detalles completos
)

enum class EstadoPedido(val estado: String) {
    PENDIENTE("Pendiente"),
    EN_PROCESO("En Proceso"),
    ENVIADO("Enviado"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    companion object {
        fun fromString(value: String): EstadoPedido {
            return when (value) {
                "Pendiente" -> PENDIENTE
                "En Proceso" -> EN_PROCESO
                "Enviado" -> ENVIADO
                "Entregado" -> ENTREGADO
                "Cancelado" -> CANCELADO
                else -> PENDIENTE
            }
        }
    }
}