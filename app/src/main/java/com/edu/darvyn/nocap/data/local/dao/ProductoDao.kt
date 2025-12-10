package com.edu.darvyn.nocap.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.darvyn.nocap.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Upsert
    suspend fun saveProducto(productoEntity: ProductoEntity)

    @Upsert
    suspend fun saveListProductos(listProductos: List<ProductoEntity>)

    @Query("""
        SELECT * FROM productoentities 
        WHERE (:marcaIds IS NULL OR marcaId IN (:marcaIds))
        ORDER BY nombre ASC
    """)
    suspend fun getProductosByMarcas(marcaIds: List<Int>?): List<ProductoEntity>

    @Query(
        """
            Select *
            from  productoentities
            where productoId =:id
            Limit 1
        """
    )
    suspend fun observeById(id: Int) : ProductoEntity

    @Query("""
        Select *
        From ProductoEntities
    """)
    fun observeAllProductos() : Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productoentities ORDER BY nombre ASC")
    suspend fun getAllProductos(): List<ProductoEntity>

    @Query(
        """
            DELETE
            from ProductoEntities
            where productoId =:id
        """
    )
    suspend fun deleteById(id: Int)

    @Query(
        """
            Delete From ProductoEntities
        """
    )
    suspend fun deleteAll()
}