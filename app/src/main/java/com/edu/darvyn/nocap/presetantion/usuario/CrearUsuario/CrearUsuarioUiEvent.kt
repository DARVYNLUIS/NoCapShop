package com.edu.darvyn.nocap.presetantion.usuario.CrearUsuario

sealed interface CrearUsuarioUiEvent {
    data class NombresChange(val nombres: String) : CrearUsuarioUiEvent
    data class EmailChange(val email: String) : CrearUsuarioUiEvent
    data class PasswordChange(val password: String) : CrearUsuarioUiEvent
    data class ConfirmPasswordChange(val confirmPassword: String) : CrearUsuarioUiEvent
    object Save : CrearUsuarioUiEvent
}