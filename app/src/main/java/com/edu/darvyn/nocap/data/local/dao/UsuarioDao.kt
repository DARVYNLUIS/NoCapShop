package com.edu.darvyn.nocap.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.darvyn.nocap.data.local.entities.UsuarioEntity

@Dao
interface UsuarioDao {
    @Upsert()
    suspend fun save (usuario: UsuarioEntity)

    @Query(
        """
            Select *
            from usuariosEntity
            limit 1
        """
    )
    suspend fun getUsuario() : UsuarioEntity?

    @Query("Delete From usuariosEntity")
    suspend fun deleteAll()
}