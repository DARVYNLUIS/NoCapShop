package com.edu.darvyn.nocap.presetantion.usuario.PerfilUsuario

sealed interface PerfilUsuarioUiEvent {
    data object LoadUsuario :  PerfilUsuarioUiEvent
    data class DeleteCuenta(val id: Int) : PerfilUsuarioUiEvent
    data object CerrarSesion : PerfilUsuarioUiEvent
}