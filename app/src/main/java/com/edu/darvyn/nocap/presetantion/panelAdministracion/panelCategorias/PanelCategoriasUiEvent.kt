package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelCategorias

sealed interface PanelCategoriasUiEvent {
    data object Load : PanelCategoriasUiEvent
}