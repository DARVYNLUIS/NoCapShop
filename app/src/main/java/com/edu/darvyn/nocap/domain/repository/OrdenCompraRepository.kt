package com.edu.darvyn.nocap.domain.repository

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.OrdenCompra
import kotlinx.coroutines.flow.Flow

interface OrdenCompraRepository {
    suspend fun crearOrden(
        carritoId: Int,
        usuarioId: Int,
        pagarAhora: Boolean
    ): Resource<OrdenCompra>

    suspend fun getById(id: Int): OrdenCompra
    fun observeById(id: Int): Flow<Resource<OrdenCompra>>
    fun observeByUsuario(usuarioId: Int): Flow<Resource<List<OrdenCompra>>>
    fun observePendientesByUsuario(usuarioId: Int): Flow<Resource<List<OrdenCompra>>>
    fun observePagadasByUsuario(usuarioId: Int): Flow<Resource<List<OrdenCompra>>>
    suspend fun marcarComoPagado(ordenId: Int): Resource<Unit>
    suspend fun countPagosPendientes(usuarioId: Int): Int
}