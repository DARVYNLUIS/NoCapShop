package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelMarcas

import com.edu.darvyn.nocap.domain.model.Marcas

data class PanelMarcasUiState (
    val isLoading: Boolean = false,
    val marcas: List <Marcas> = emptyList(),
    val message: String? = null
)