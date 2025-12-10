package com.edu.darvyn.nocap.data.remote.dto

data class UsuarioDto(
    val usuarioId: Int?,
    val nombres: String,
    val correo: String,
    val contrasena: String?,
    val rolId: Int?
)