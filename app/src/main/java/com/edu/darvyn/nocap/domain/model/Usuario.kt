package com.edu.darvyn.nocap.domain.model

data class Usuario (
    val usuarioId: Int?,
    val nombres: String,
    val email: String,
    val password: String?,
    val confirmPassword: String?,
    val rol: Int?
)