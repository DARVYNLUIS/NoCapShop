package com.edu.darvyn.nocap.presetantion.categoria.edit


data class EditCategoriaUiState(
    val categoriaId: Int? = null,
    val categoriaNombre: String? = null,
    val activa: Boolean? = false,
    val errorCategoriaNombre: String? = null,
    val categoriaDescripcion: String? = null,
    val errorCategoriaDescripcion: String? = null,
    val isSaving: Boolean = false,
    val isNew: Boolean = false,
    val saved: Boolean = false,
)