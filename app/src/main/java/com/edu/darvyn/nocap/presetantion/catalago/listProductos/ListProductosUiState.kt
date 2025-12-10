package com.edu.darvyn.nocap.presetantion.catalago.listProductos

import com.edu.darvyn.nocap.domain.model.Marcas
import com.edu.darvyn.nocap.domain.model.Producto

data class CatalogoUiState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val selectedMarcaIds: List<Int>? = emptyList(),
    val marcas: List<Marcas> = emptyList(),
    val mostrarModal: Boolean = false,
    val observerProducto: Int? = null,
    val message: String? = null,
)