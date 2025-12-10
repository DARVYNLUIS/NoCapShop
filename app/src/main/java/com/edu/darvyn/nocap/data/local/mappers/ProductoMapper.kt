package com.edu.darvyn.nocap.data.local.mappers

import com.edu.darvyn.nocap.data.local.entities.ProductoEntity
import com.edu.darvyn.nocap.data.remote.dto.ProductosDto
import com.edu.darvyn.nocap.domain.model.Producto

fun ProductosDto.dtoToEntity(): ProductoEntity = ProductoEntity(
    productoId = productoId,
    productoImagen = productoImagne,
    nombre = productoNombre,
    descripcion = productoDescripcion,
    precioVenta = precioProductoVenta,
    categoriaId = categoriaId,
    marcaId = marcaId,
    fechaCreacion = fechaCreacionProducto,
    stocks = stocks,
    listaColores = colores,
    listaTamanos = tamaños,
    activo = activo
)

fun ProductoEntity.toDomain(): Producto = Producto(
    productoId = productoId,
    nombre = nombre,
    descripcion = descripcion,
    productoImagen = productoImagen,
    precioVenta = precioVenta,
    categoriaId = categoriaId,
    marcaId = marcaId,
    fechaCreacion = fechaCreacion,
    stocks = stocks,
    listaColores = listaColores,
    listaTamanos = listaTamanos,
    activo = activo

)

fun Producto.toEntity(): ProductoEntity = ProductoEntity(
    productoId = productoId,
    nombre = nombre,
    descripcion = descripcion,
    productoImagen = productoImagen,
    precioVenta = precioVenta,
    categoriaId = categoriaId,
    marcaId = marcaId,
    fechaCreacion = fechaCreacion,
    stocks = stocks,
    listaColores = listaColores,
    listaTamanos = listaTamanos,
    activo = activo

)

fun Producto.toDto(): ProductosDto = ProductosDto(
    productoId = productoId,
    productoNombre = nombre,
    productoDescripcion = descripcion,
    productoImagne = productoImagen,
    precioProductoVenta = precioVenta,
    categoriaId = categoriaId,
    marcaId = marcaId,
    fechaCreacionProducto = fechaCreacion,
    stocks = stocks,
    colores = listaColores,
    tamaños = listaTamanos,
    activo = activo

)