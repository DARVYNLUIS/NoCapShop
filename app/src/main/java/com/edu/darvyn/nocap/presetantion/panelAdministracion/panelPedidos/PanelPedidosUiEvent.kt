package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelPedidos


sealed interface PanelPedidosUiEvent {
    data object Load : PanelPedidosUiEvent
}