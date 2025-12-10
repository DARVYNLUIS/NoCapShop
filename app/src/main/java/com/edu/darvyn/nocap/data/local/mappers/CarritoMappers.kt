package com.edu.darvyn.nocap.data.local.mappers

import com.edu.darvyn.nocap.data.local.entities.CarritoDetailWithProducto
import com.edu.darvyn.nocap.data.local.entities.CarritoDetailsEntity
import com.edu.darvyn.nocap.domain.model.CarritoItem

fun CarritoDetailWithProducto.toCarritoItem() = CarritoItem(
    carritoDetailsId = detail.carritoDetailsId,
    productoId = detail.productoId,
    productoNombre = producto?.nombre ?: "",
    productoImagen = producto?.productoImagen ?: null,
    cantidad = detail.cantidad,
    precioProducto = detail.precioProducto,
    color = detail.color,
    talla = detail.talla
)
fun List<CarritoDetailWithProducto>.toCarritoItems() = map { it.toCarritoItem() }

fun List<CarritoDetailsEntity>.toCarritoItemsCompatible(): List<CarritoItem> {
    return map {
        CarritoItem(
            carritoDetailsId = it.carritoDetailsId,
            productoId = it.productoId,
            productoNombre = "", // No hay producto vinculado aquí
            productoImagen = null,
            cantidad = it.cantidad,
            precioProducto = it.precioProducto,
            color = it.color,
            talla = it.talla
        )
    }
}

