package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelProductos

sealed interface  PanelProductosUiEvent {
    data object Load : PanelProductosUiEvent
}