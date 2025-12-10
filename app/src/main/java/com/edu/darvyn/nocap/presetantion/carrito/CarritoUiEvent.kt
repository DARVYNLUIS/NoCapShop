package com.edu.darvyn.nocap.presetantion.carrito

sealed class CarritoUiEvent {

    object Load : CarritoUiEvent()

    object LoadUsuario : CarritoUiEvent()
    data class EliminarItem(val carritoDetailsId: Int) : CarritoUiEvent()
    data class ActualizarCantidad(val carritoId: Int?, val carritoDetailsId: Int, val cantidad: Int) : CarritoUiEvent()
    object LimpiarCarrito : CarritoUiEvent()
    object DismissError : CarritoUiEvent()
}