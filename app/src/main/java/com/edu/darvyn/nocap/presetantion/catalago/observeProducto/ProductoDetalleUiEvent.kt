package com.edu.darvyn.nocap.presetantion.catalago.observeProducto

sealed interface ProductoDetalleUiEvent {

    data object LoadUsuario : ProductoDetalleUiEvent
    data class SelectTalla(val talla: String) : ProductoDetalleUiEvent
    data class SelectColor(val color: String) : ProductoDetalleUiEvent
    data class ChangeCantidad(val cantidad: Int) : ProductoDetalleUiEvent
    data object AgregarAlCarrito : ProductoDetalleUiEvent

    data class LoadProducto(val productoId : Int) : ProductoDetalleUiEvent
}