package com.edu.darvyn.nocap.presetantion.usuario.Login

sealed interface LoginUiEvent {
    data object IniciarSesion : LoginUiEvent
    data object LoadUsuario : LoginUiEvent
    data class NombreChange(val nombre: String) : LoginUiEvent
    data class PasswordChange(val password : String) : LoginUiEvent
    data class IsVisbleChange(val isVisible: Boolean) : LoginUiEvent

}