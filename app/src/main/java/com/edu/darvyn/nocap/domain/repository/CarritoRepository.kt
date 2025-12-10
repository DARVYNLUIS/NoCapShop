package com.edu.darvyn.nocap.domain.repository

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Carrito
import kotlinx.coroutines.flow.Flow

interface CarritoRepository {
    fun observeCarrito(usuarioId: Int): Flow<Resource<Carrito>>

    suspend  fun getById(carritoId: Int): Carrito

    suspend fun agregarProducto(
        usuarioId: Int,
        productoId: Int,
        cantidad: Int,
        precio: Double,
        color: String,
        talla: String
    ): Resource<Unit>

    suspend fun eliminarItem(carritoDetailsId: Int): Resource<Unit>
    suspend fun actualizarCantidad(carritoDetailsId: Int, cantidad: Int): Resource<Unit>
    suspend fun limpiarCarrito(carritoId: Int): Resource<Unit>

    suspend fun marcarComoPedido(carritoId: Int): Resource<Unit>


}