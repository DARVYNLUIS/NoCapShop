package com.edu.darvyn.nocap.presetantion.ordenCompras.OrdenCompraDetails

import com.edu.darvyn.nocap.domain.model.Carrito
import com.edu.darvyn.nocap.domain.model.OrdenCompra

data class OrdenCompraUiState (
    val isLoading: Boolean = false,
    val carrito: Carrito? = null,
    val ordenCreada: OrdenCompra? = null,
    val error: String? = null,
    val subtotal: Double = 0.0,
    val itbis: Double = 0.0,
    val envio: Double = 0.0,
    val total: Double = 0.0,
    val envioGratis: Boolean = false,
    val pagosPendientes: Int = 0,
    val maxPagosPendientes: Int = 3,
    val showConfirmation: Boolean = false,
)