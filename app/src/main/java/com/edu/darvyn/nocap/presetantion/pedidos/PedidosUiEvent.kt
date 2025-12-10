package com.edu.darvyn.nocap.presetantion.pedidos

import com.edu.darvyn.nocap.domain.model.EstadoPedido

sealed class PedidosUiEvent {
    object LoadPedidosByUsuario : PedidosUiEvent()
    data class LoadPedidoById(val pedidoId: Int) : PedidosUiEvent()
    data class CrearPedido(
        val ordenCompraId: Int,
        val usuarioId: Int,
        val direccionEnvio: String?
    ) : PedidosUiEvent()
    data class ActualizarEstado(val pedidoId: Int, val nuevoEstado: EstadoPedido) : PedidosUiEvent()
    data class CancelarPedido(val pedidoId: Int) : PedidosUiEvent()
    data class FiltrarPorEstado(val estado: EstadoPedido?) : PedidosUiEvent()
    object ClearSuccess : PedidosUiEvent()
    object ClearError : PedidosUiEvent()
}