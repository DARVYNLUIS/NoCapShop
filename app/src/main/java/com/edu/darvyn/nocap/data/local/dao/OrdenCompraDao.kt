package com.edu.darvyn.nocap.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.darvyn.nocap.data.local.entities.OrdenCompraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdenCompraDao {
    @Upsert()
    suspend fun saveOrdenCompra(ordenCompraEntity: OrdenCompraEntity) : Long

    @Query("SELECT * FROM OrdenCompraEnities WHERE ordenCompraId = :id LIMIT 1")
    suspend fun getById(id: Int): OrdenCompraEntity?

    @Query("SELECT * FROM OrdenCompraEnities WHERE carritoId = :id LIMIT 1")
    suspend fun getByCarritoId(id: Int): OrdenCompraEntity?

    @Query("SELECT * FROM OrdenCompraEnities WHERE ordenCompraId = :id LIMIT 1")
    fun observeById(id: Int): Flow<OrdenCompraEntity?>

    @Query("SELECT * FROM OrdenCompraEnities WHERE usuarioId = :usuarioId ORDER BY fechaCompra DESC")
    fun observeByUsuario(usuarioId: Int): Flow<List<OrdenCompraEntity>>

    @Query("UPDATE OrdenCompraEnities SET pagado = :pagado WHERE ordenCompraId = :id")
    suspend fun updatePagado(id: Int, pagado: Boolean)

    @Query("SELECT COUNT(*) FROM OrdenCompraEnities WHERE usuarioId = :usuarioId AND pagado = 0")
    suspend fun countPagosPendientes(usuarioId: Int): Int

    @Query("SELECT * FROM OrdenCompraEnities WHERE usuarioId = :usuarioId AND pagado = 0")
    fun observePendientesByUsuario(usuarioId: Int): Flow<List<OrdenCompraEntity>>

    @Query("SELECT * FROM OrdenCompraEnities WHERE usuarioId = :usuarioId AND pagado = 1")
    fun observePagadasByUsuario(usuarioId: Int): Flow<List<OrdenCompraEntity>>
}