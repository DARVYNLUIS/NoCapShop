package com.edu.darvyn.nocap.presetantion.ordenCompras.OrdenCompraDetails

sealed interface OrdenCompraUiEvent {

    data class Load(val carritoId: Int?) : OrdenCompraUiEvent

    data class LoadOrden(val ordenId:Int?) : OrdenCompraUiEvent
    data class PagarAhora(val usuarioId: Int?) : OrdenCompraUiEvent
    data class PagarDespues(val usuarioId: Int?) : OrdenCompraUiEvent
    object DismissConfirmation : OrdenCompraUiEvent
    object DismissError : OrdenCompraUiEvent
    object VolverAInicio : OrdenCompraUiEvent
}