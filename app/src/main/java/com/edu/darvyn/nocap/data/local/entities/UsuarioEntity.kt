package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuariosEntity")
data class UsuarioEntity(
    @PrimaryKey
    val usuarioId: Int?,
    val nombres: String,
    val email: String,
    val rol: Int
)