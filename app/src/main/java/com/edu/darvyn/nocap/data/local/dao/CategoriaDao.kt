package com.edu.darvyn.nocap.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.darvyn.nocap.data.local.entities.CategoriasEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Upsert
    suspend fun save(categoria: CategoriasEntity)

    @Upsert
    suspend fun saveList(categorias: List<CategoriasEntity>)

    @Query(
        """
            Select * 
            from CategoriaEntities
            where categoriaId =:categoriaid
            limit 1
        """
    )
    suspend fun getCategoria(categoriaid: Int) : CategoriasEntity

    @Query(
        """
            select * 
            from CategoriaEntities
        """
    )
    fun getAll() : Flow<List<CategoriasEntity>>

    @Query (
        """
            Delete from CategoriaEntities
             where categoriaId =:categoriaid 
        """
    )
    suspend fun eliminarCategoria(categoriaid: Int)

    @Query(
        """
            Delete 
            from CategoriaEntities
        """
    )
    suspend fun deleteAll()

}