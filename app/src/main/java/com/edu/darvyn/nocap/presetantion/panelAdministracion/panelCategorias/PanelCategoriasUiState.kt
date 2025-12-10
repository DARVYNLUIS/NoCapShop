package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelCategorias

import com.edu.darvyn.nocap.domain.model.Categoria

data class PanelCategoriasUiState(
    val isLoading: Boolean = false,
    val categorias: List <Categoria> = emptyList(),
    val message: String? = null
)