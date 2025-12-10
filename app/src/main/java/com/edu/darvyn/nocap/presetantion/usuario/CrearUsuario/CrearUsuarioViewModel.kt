package com.edu.darvyn.nocap.presetantion.usuario.CrearUsuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.domain.model.Usuario
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.CrearUsuario.CrearUsuarioUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validateConfirmationPasswordUser
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validateEmailUser
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validateNombreUser
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validatePasswordUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrearUsuarioViewModel @Inject constructor(
    private val crearUsuarioUseCase: CrearUsuarioUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CrearUsuarioUiState())
    val state: StateFlow<CrearUsuarioUiState> = _state.asStateFlow()


    fun onEvent(event: CrearUsuarioUiEvent) {
        when (event) {
            is CrearUsuarioUiEvent.ConfirmPasswordChange -> _state.update {
                it.copy(confirmPassword = event.confirmPassword)
            }

            is CrearUsuarioUiEvent.EmailChange -> _state.update {
                it.copy(email = event.email)
            }

            is CrearUsuarioUiEvent.NombresChange -> _state.update {
                it.copy(nombres = event.nombres)
            }

            is CrearUsuarioUiEvent.PasswordChange -> _state.update {
                it.copy(password = event.password)
            }

            CrearUsuarioUiEvent.Save -> crearUsuario()
        }
    }

    fun crearUsuario() {
        viewModelScope.launch {
            if (validar()){
                _state.update { it.copy(isLoading = true) }
                val result = crearUsuarioUseCase(_state.value.toDto())
                result.onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isSaved = true
                        )
                    }
                }.onFailure { e ->
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun validar(): Boolean {
        var emailCorrecto =
            validateEmailUser(_state.value.email.toString())
        var passwordCorrecto =
            validatePasswordUser(_state.value.password.toString())
        var confirmpassword =
            validateConfirmationPasswordUser(_state.value.password.toString(),
                _state.value.confirmPassword.toString())
        var nombreCorrecto =
            validateNombreUser(_state.value.nombres.toString())

        _state.update {
            it.copy(
                errorNombre = nombreCorrecto.error,
                errorEmail = emailCorrecto.error,
                errorPassword = passwordCorrecto.error,
                errorConfirmPassword = confirmpassword.error
            )
        }
        return passwordCorrecto.isValid == true && emailCorrecto.isValid == true && confirmpassword.isValid == true && nombreCorrecto.isValid == true
    }

    fun CrearUsuarioUiState.toDto() : Usuario = Usuario(
        usuarioId = 0,
        nombres = _state.value.nombres!!,
        email = _state.value.email!!,
        password = _state.value.password!!,
        confirmPassword = _state.value.confirmPassword!!,
        rol = null
    )

}