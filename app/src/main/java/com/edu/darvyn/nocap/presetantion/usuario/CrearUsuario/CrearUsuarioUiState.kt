package com.edu.darvyn.nocap.presetantion.usuario.CrearUsuario

data class CrearUsuarioUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val nombres: String? = null,
    val errorNombre: String? = null,
    val email: String? = null ,
    val errorEmail: String? = null,
    val password: String? = null,
    val errorPassword: String? = null,
    val confirmPassword: String? = null,
    val errorConfirmPassword: String? = null,
    val message: String? = null
)