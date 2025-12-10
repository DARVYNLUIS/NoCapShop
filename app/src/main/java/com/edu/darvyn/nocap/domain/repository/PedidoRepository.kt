package com.edu.darvyn.nocap.domain.repository

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.EstadoPedido
import com.edu.darvyn.nocap.domain.model.Pedido
import kotlinx.coroutines.flow.Flow

interface PedidoRepository {

    suspend fun crearPedido(
        ordenCompraId: Int,
        usuarioId: Int,
    ): Resource<Pedido>

    suspend fun getById(id: Int): Pedido?
    suspend fun getAllPedidos(): List<Pedido?>
    fun observeById(id: Int): Flow<Resource<Pedido>>

    fun observeByUsuario(usuarioId: Int): Flow<Resource<List<Pedido>>>

    suspend fun getByOrdenCompra(ordenCompraId: Int): Pedido?

    fun observeByOrdenCompra(ordenCompraId: Int): Flow<Resource<Pedido>>

    suspend fun actualizarEstado(pedidoId: Int, estado: EstadoPedido): Resource<Unit>


    fun observeByEstado(usuarioId: Int, estado: EstadoPedido): Flow<Resource<List<Pedido>>>

    suspend fun countPedidosActivos(usuarioId: Int): Int

    suspend fun cancelarPedido(pedidoId: Int): Resource<Unit>
}