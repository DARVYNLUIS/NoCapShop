package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "CategoriaEntities")
data class CategoriasEntity (
    @PrimaryKey
    val categoriaId: Int?,
    val nombre: String,
    val descripcion: String,
    val activa: Boolean
)