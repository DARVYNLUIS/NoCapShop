package com.edu.darvyn.nocap.presetantion.catalago.listProductos

sealed interface ListProductosUiEvent {
    data object LoadProductos: ListProductosUiEvent
    data object LoadMarcas: ListProductosUiEvent

    data object LimpiarFiltro: ListProductosUiEvent

    data object AplicarFiltro: ListProductosUiEvent

    data class ModalOn(val state: Boolean): ListProductosUiEvent

    data class ShowMessage(val message: String): ListProductosUiEvent
    data class SeleccionarMarcar(val marcaId: Int) : ListProductosUiEvent
}