package com.edu.darvyn.nocap.presetantion.marcas.edit

data class EditMarcaUiState(
    val marcaId: Int? = null,
    val marcaNombre: String? = null,
    val errorMarcaNombre: String? = null,
    val activa: Boolean? = null,
    val isSaving: Boolean = false,
    val isNew: Boolean = false,
    val saved: Boolean = false,
)