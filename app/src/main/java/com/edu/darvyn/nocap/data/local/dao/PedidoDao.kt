package com.edu.darvyn.nocap.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.darvyn.nocap.data.local.entities.PedidosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Upsert
    suspend fun savePedido(pedido: PedidosEntity): Long

    @Query("SELECT * FROM PedidoEntities WHERE pedidoId = :id LIMIT 1")
    suspend fun getById(id: Int): PedidosEntity?

    @Query("SELECT * FROM PedidoEntities")
    suspend fun getAllPedidos() : List<PedidosEntity>

    @Query("SELECT * FROM PedidoEntities WHERE pedidoId = :id LIMIT 1")
    fun observeById(id: Int): Flow<PedidosEntity?>

    @Query("SELECT * FROM PedidoEntities WHERE usuarioId = :usuarioId ORDER BY fechaCreacion DESC")
    fun observeByUsuario(usuarioId: Int): Flow<List<PedidosEntity>>

    @Query("SELECT * FROM PedidoEntities WHERE ordenCompraId = :ordenCompraId LIMIT 1")
    suspend fun getByOrdenCompra(ordenCompraId: Int): PedidosEntity?

    @Query("SELECT * FROM PedidoEntities WHERE ordenCompraId = :ordenCompraId LIMIT 1")
    fun observeByOrdenCompra(ordenCompraId: Int): Flow<PedidosEntity?>

    @Query("UPDATE PedidoEntities SET estado = :estado WHERE pedidoId = :id")
    suspend fun updateEstado(id: Int, estado: String)

    @Query("SELECT * FROM PedidoEntities WHERE usuarioId = :usuarioId AND estado = :estado ORDER BY fechaCreacion DESC")
    fun observeByUsuarioAndEstado(usuarioId: Int, estado: String): Flow<List<PedidosEntity>>

    @Query("SELECT COUNT(*) FROM PedidoEntities WHERE usuarioId = :usuarioId AND estado IN ('Pendiente', 'En Proceso', 'Enviado')")
    suspend fun countPedidosActivos(usuarioId: Int): Int

    @Query("DELETE FROM PedidoEntities WHERE pedidoId = :id")
    suspend fun deletePedido(id: Int)
}