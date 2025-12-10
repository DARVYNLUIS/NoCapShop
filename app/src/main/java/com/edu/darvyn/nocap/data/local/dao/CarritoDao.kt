package com.edu.darvyn.nocap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.edu.darvyn.nocap.data.local.entities.CarritoDetailsEntity
import com.edu.darvyn.nocap.data.local.entities.CarritoEntity
import com.edu.darvyn.nocap.data.local.entities.CarritoWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    @Upsert
    suspend fun saveCarrito(carrito: CarritoEntity)

    @Insert
    suspend fun insertCarrito(carrito: CarritoEntity): Long

    @Query("SELECT * FROM CarritoEntities WHERE carritoId = :id LIMIT 1")
    suspend fun observeById(id: Int): CarritoEntity?
    @Transaction

    @Query("SELECT * FROM CarritoEntities WHERE carritoId = :id LIMIT 1")
    suspend fun getById(id: Int): CarritoWithDetails?

    @Transaction
    @Query("SELECT * FROM CarritoEntities WHERE carritoId = :carritoId LIMIT 1")
    suspend fun getCarritoWithDetailsById(carritoId: Int): CarritoWithDetails?

    @Query("SELECT * FROM CarritoEntities WHERE usuarioId = :usuarioId AND pedido = 0 LIMIT 1")
    suspend fun getCarritoActivo(usuarioId: Int): CarritoEntity?

    @Query("SELECT * FROM CarritoEntities WHERE usuarioId = :usuarioId AND pedido = 0 LIMIT 1")
    fun observeCarritoByUsuario(usuarioId: Int): Flow<CarritoEntity?>
    @Query("DELETE FROM CarritoEntities WHERE carritoId = :id")
    suspend fun deleteCarrito(id: Int)

    @Transaction
    @Query("SELECT * FROM CarritoEntities WHERE usuarioId = :usuarioId AND pedido = 0 LIMIT 1")
    fun observeCarritoWithDetails(usuarioId: Int): Flow<CarritoWithDetails?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarritoDetail(detail: CarritoDetailsEntity)

    @Query("DELETE FROM CarritoDetailsEntities WHERE carritoDetailsId = :id")
    suspend fun deleteCarritoDetail(id: Int)

    @Query("UPDATE CarritoDetailsEntities SET cantidad = :cantidad WHERE carritoDetailsId = :id")
    suspend fun updateCantidad(id: Int, cantidad: Int)

    @Query("SELECT * FROM CarritoDetailsEntities WHERE carritoId = :carritoId")
    fun observeCarritoDetails(carritoId: Int): Flow<List<CarritoDetailsEntity>>

    @Query("UPDATE CarritoEntities SET pedido = 1 WHERE carritoId = :carritoId")
    suspend fun marcarComoPedido(carritoId: Int)
}