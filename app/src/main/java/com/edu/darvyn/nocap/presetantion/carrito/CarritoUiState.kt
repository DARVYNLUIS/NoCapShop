package com.edu.darvyn.nocap.presetantion.carrito

import com.edu.darvyn.nocap.domain.model.Carrito
import com.edu.darvyn.nocap.domain.model.Usuario

data class CarritoUiState(
    val isLoading: Boolean = false,
    val user: Usuario? = null,
    val carrito: Carrito? = null,
    val error: String? = null,
    val subtotal: Double = 0.0,
    val envio: Double = 0.0,
    val total: Double = 0.0,
    val envioGratis: Boolean = false
)