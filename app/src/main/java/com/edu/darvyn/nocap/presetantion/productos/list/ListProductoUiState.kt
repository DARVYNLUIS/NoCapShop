package com.edu.darvyn.nocap.presetantion.productos.list

import com.edu.darvyn.nocap.domain.model.Producto

data class ListProductoUiState (
    val isLoading: Boolean = false,
    val listProducto: List<Producto> = emptyList(),
    val message: String? = null,
    val isDeleting: Boolean = false,
    val productoId : Int? = null
)