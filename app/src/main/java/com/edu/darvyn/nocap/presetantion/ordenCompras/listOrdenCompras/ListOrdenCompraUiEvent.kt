package com.edu.darvyn.nocap.presetantion.ordenCompras.listOrdenCompras

sealed class OrdenesListUiEvent {
    data class LoadOrdenes(val usuarioId: Int) : OrdenesListUiEvent()
    data class FiltrarPorEstado(val filtro: FiltroEstado) : OrdenesListUiEvent()
}