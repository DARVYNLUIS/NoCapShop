package com.edu.darvyn.nocap.presetantion.catalago.observeProducto

import com.edu.darvyn.nocap.domain.model.Producto
import com.edu.darvyn.nocap.domain.model.Usuario

data class ProductoDetalleUiState (
    val productoId : Int? = null,
    val producto: Producto? = null,
    val user: Usuario? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTalla: String? = null,
    val selectedColor: String? = null,
    val cantidad: Int = 1,
    val mensajeExito: String? = null
)