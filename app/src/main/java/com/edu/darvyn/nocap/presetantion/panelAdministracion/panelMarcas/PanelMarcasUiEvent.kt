package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelMarcas

sealed interface PanelMarcasUiEvent {
    data object Load : PanelMarcasUiEvent
}