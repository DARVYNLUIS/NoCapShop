package com.edu.darvyn.nocap.domain.model

class CarritoItem (
    val carritoDetailsId: Int?,
    val productoId: Int,
    val productoNombre: String,
    val productoImagen: String?,
    val cantidad: Int,
    val precioProducto: Double,
    val color: String,
    val talla: String
) {
    val subtotal: Double
        get() = cantidad * precioProducto
}