package com.edu.darvyn.nocap.presetantion.categoria.list

import com.edu.darvyn.nocap.domain.model.Categoria


data class ListCategoriaUiState(
    val isLoading: Boolean = false,
    val listCategorias: List<Categoria> = emptyList(),
    val message: String? = null,
    val isDeleting: Boolean = false,
    val categoriaId : Int? = null
)