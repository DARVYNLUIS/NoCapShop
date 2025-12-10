package com.edu.darvyn.nocap.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.darvyn.nocap.data.local.entities.MarcasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarcasDao {
    @Upsert
    suspend fun save (marcas: MarcasEntity)

    @Upsert
    suspend fun saveList (marcas: List<MarcasEntity>)

    @Query(
        """
            select * 
            from MarcasEntities
            where marcaId =:marcaId
        """
    )
    suspend fun getMarcaById(marcaId: Int) : MarcasEntity

    @Query(
        """
            select * 
            from MarcasEntities
        """
    )
    fun getAll() : Flow<List<MarcasEntity>>

    @Query(
        """
            Delete
            from MarcasEntities
            where marcaId =:marcaId
        """
    )
    suspend fun deleteById(marcaId: Int)

    @Query(
        """Delete from MarcasEntities"""
    )
    suspend fun deleteAll()
}