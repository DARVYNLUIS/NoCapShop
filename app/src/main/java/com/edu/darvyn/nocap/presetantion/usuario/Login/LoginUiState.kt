package com.edu.darvyn.nocap.presetantion.usuario.Login

data class LoginUiState (
    val isLoading : Boolean = false,
    val isExiste : Boolean = false,
    val usuarioId: Int? = null,
    val email : String? = null,
    val emailError: String? = null,
    val password : String? = null,
    val passwordError: String? = null,
    val isVisiblepassword: Boolean = false
)