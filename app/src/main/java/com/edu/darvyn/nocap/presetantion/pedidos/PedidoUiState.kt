package com.edu.darvyn.nocap.presetantion.pedidos

import com.edu.darvyn.nocap.domain.model.Pedido

data class PedidoUiState(
    val pedidos: List<Pedido> = emptyList(),
    val pedidoSeleccionado: Pedido? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showSuccess: Boolean = false,
    val successMessage: String? = null
)