package com.edu.darvyn.nocap.presetantion.usuario.PerfilUsuario

import com.edu.darvyn.nocap.domain.model.Usuario

data class PerfilUsuarioUiState (
    val usuario: Usuario? = null,
    val isDelete: Boolean = false,
    val isClosedSession: Boolean = false,
    val message: String? = null
)