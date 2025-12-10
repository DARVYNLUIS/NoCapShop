package com.edu.darvyn.nocap.domain.model

data class Producto (
    val productoId : Int?,
    val nombre: String,
    val descripcion: String,
    val fechaCreacion: String?,
    val productoImagen: String?,
    val precioVenta: Double,
    val stocks: Int,
    val categoriaId: Int,
    val marcaId: Int,
    val activo: Boolean,
    val listaTamanos: List<String>,
    val listaColores: List<String>
)