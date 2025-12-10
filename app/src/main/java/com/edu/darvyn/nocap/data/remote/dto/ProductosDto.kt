package com.edu.darvyn.nocap.data.remote.dto

data class ProductosDto (
    val productoId : Int?,
    val productoNombre: String,
    val productoDescripcion: String,
    val productoImagne: String?,
    val fechaCreacionProducto: String?,
    val precioProductoVenta: Double,
    val stocks: Int,
    val categoriaId: Int,
    val marcaId: Int,
    val activo: Boolean,
    val tamanos: List<String>,
    val colores: List<String>
)