package com.edu.darvyn.nocap.presetantion.productos.list

sealed interface ListProductoUiEvent {
    data object Load : ListProductoUiEvent
    data class Edit(val id: Int?) : ListProductoUiEvent
    data class Delete(val productoId: Int) : ListProductoUiEvent
    data class ShowMessage(val message: String) : ListProductoUiEvent
}