package com.edu.darvyn.nocap.presetantion.categoria.list

sealed interface ListCategoriaUiEvent {
    data object Load : ListCategoriaUiEvent
    data class Edit(val id: Int?) : ListCategoriaUiEvent
    data class Delete(val categoriaId: Int) : ListCategoriaUiEvent
    data class ShowMessage(val message: String) : ListCategoriaUiEvent
}