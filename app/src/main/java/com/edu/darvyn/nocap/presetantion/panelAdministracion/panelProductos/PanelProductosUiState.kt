package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelProductos

import com.edu.darvyn.nocap.domain.model.Producto

data class PanelProductosUiState(
    val isLoading: Boolean = false,
    val productos: List <Producto> = emptyList(),
    val message: String? = null
)