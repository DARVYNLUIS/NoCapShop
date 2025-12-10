package com.edu.darvyn.nocap.presetantion.usuario.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.dto.RequestLogin
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.Login.IniciarSesionUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.Login.ObserveUserLoggedUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validateEmailUser
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validatePasswordUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val iniciarSesionUseCase : IniciarSesionUseCase,
    private val observeUserLoggedUseCase : ObserveUserLoggedUseCase
): ViewModel() {
    private val _state = MutableStateFlow(LoginUiState(isLoading = true))
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    init {
        onEvent(LoginUiEvent.LoadUsuario)
    }

    fun onEvent(event: LoginUiEvent) {
        when(event) {
            LoginUiEvent.IniciarSesion -> iniciarSesion()
            LoginUiEvent.LoadUsuario -> getUsuario()
            is LoginUiEvent.NombreChange -> _state.update{
                it.copy( email = event.nombre, emailError = null) }
            is LoginUiEvent.PasswordChange -> _state.update{
                it.copy( password = event.password, emailError = null) }
            is LoginUiEvent.IsVisbleChange -> _state.update{
                it.copy( isVisiblepassword = event.isVisible, emailError = null) }
        }
    }

    fun iniciarSesion(){
        viewModelScope.launch {
            if (validar()) {
                _state.update { it.copy(isLoading = true) }
                val result = iniciarSesionUseCase(_state.value.toRequest())
                result.onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isExiste = true,
                        )
                    }
                }.onFailure { e ->
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun getUsuario(){
        viewModelScope.launch {
            val user = observeUserLoggedUseCase()
            _state.update {
                it.copy(
                    isLoading =  user != null,
                    isExiste = user != null,
                    usuarioId = user?.usuarioId
                )
            }
        }
    }
    private fun validar(): Boolean {
        var emailCorrecto =
            validateEmailUser(_state.value.email.toString())
        var passwordCorrecto =
            validatePasswordUser(_state.value.password.toString())

        _state.update {
            it.copy(
                emailError = emailCorrecto.error,
                passwordError = passwordCorrecto.error
            )
        }

        return passwordCorrecto.isValid == true && emailCorrecto.isValid == true
    }
    fun LoginUiState.toRequest() : RequestLogin = RequestLogin(
        email = _state.value.email!!,
        password = _state.value.password!!
    )
}
