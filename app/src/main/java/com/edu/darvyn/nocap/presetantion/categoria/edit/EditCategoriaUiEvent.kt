package com.edu.darvyn.nocap.presetantion.categoria.edit

sealed interface EditCategoriaUiEvent {
    data class LoadCategoria (val id: Int) : EditCategoriaUiEvent
    data class CategoriaNombreChange(val nombre: String) : EditCategoriaUiEvent
    data class CategoriaDescripcionChange(val descripcion: String) : EditCategoriaUiEvent
    data object Save: EditCategoriaUiEvent
}