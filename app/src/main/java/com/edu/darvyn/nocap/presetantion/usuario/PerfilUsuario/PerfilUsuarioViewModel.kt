package com.edu.darvyn.nocap.presetantion.usuario.PerfilUsuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.Login.CerrarSesionUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.Login.ObserveUserLoggedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilUsuarioViewModel @Inject constructor(
    private val observeUserLoggedUseCase: ObserveUserLoggedUseCase,
    private val cerrarSesionUseCase: CerrarSesionUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PerfilUsuarioUiState())
    val state: StateFlow<PerfilUsuarioUiState> = _state.asStateFlow()

    init {
        onEvent(PerfilUsuarioUiEvent.LoadUsuario)
    }

    fun onEvent(event : PerfilUsuarioUiEvent){
        when(event) {
            PerfilUsuarioUiEvent.CerrarSesion -> cerraSesion()
            is PerfilUsuarioUiEvent.DeleteCuenta -> TODO()
            PerfilUsuarioUiEvent.LoadUsuario -> observeUserLogged()
        }
    }

    fun observeUserLogged() {
        viewModelScope.launch {
            val user = observeUserLoggedUseCase()
            _state.update {
                it.copy(
                    usuario = user
                )
            }
        }
    }

    private fun cerraSesion() {
        viewModelScope.launch {
            cerrarSesionUseCase()
            _state.update {
                it.copy(
                    usuario = null
                )
            }
        }
    }
}