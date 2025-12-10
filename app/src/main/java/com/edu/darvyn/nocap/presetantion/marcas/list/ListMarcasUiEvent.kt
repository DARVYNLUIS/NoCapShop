package com.edu.darvyn.nocap.presetantion.marcas.list


sealed interface ListMarcasUiEvent {
    data object Load : ListMarcasUiEvent
    data class Edit(val id: Int?) : ListMarcasUiEvent
    data class Delete(val marcaId: Int) : ListMarcasUiEvent
    data class ShowMessage(val message: String) : ListMarcasUiEvent
}