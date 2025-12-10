package com.edu.darvyn.nocap.presetantion.productos.edit

import com.edu.darvyn.nocap.domain.model.Categoria
import com.edu.darvyn.nocap.domain.model.Marcas

data class EditProductoUiState (
    val isNew: Boolean? = false,
    val isSaving: Boolean? = false,
    val saved: Boolean? = false,
    val productoId: Int? = null,
    val nombre: String? = null,
    val nombreError: String? = null,
    val descripcion: String? = null,
    val descripcionError: String? = null,
    val precioCompra: String? = null,
    val precioCompraError: String? = null,
    val precioVenta: String? = null,
    val precioVentaError: String? = null,
    val productoImagen: String? = null,
    val productoImagenError: String? = null,
    val stocks: String? = null,
    val stocksError: String? = null,
    val categoriaId: Int? = 0,
    val categoriaError: String? = null,
    val marcaId: Int? = 0,
    val marcaError: String? = null,
    val isLoading: Boolean = false,
    val listaTamano: List<String> = emptyList(),
    val listaCategoria: List<Categoria> = emptyList(),
    val listaColores: List<String> = emptyList(),
    val listaMarcas: List<Marcas> = emptyList(),

)