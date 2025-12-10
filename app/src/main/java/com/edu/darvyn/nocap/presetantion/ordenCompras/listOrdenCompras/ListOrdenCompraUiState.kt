package com.edu.darvyn.nocap.presetantion.ordenCompras.listOrdenCompras

import com.edu.darvyn.nocap.domain.model.OrdenCompra

enum class FiltroEstado {
    TODOS, PAGADO, PENDIENTE
}

data class OrdenesListUiState(
    val isLoading: Boolean = false,
    val ordenes: List<OrdenCompra>? = emptyList(),
    val filtroEstado: FiltroEstado = FiltroEstado.TODOS,
    val error: String? = null
)