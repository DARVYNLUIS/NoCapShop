package com.edu.darvyn.nocap.presetantion.marcas.list

import com.edu.darvyn.nocap.domain.model.Marcas

data class ListMarcaUiState(
    val isLoading: Boolean = false,
    val listMarcas: List<Marcas> = emptyList(),
    val message: String? = null,
    val isDeleting: Boolean = false,
    val marcaId : Int? = null
)