package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelPedidos

import com.edu.darvyn.nocap.domain.model.Pedido


data class PanelPedidosUiState(
    val isLoading: Boolean = false,
    val pedidos: List<Pedido?> = emptyList(),
    val message: String? = null
)