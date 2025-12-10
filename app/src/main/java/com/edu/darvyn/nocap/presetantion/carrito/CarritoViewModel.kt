package com.edu.darvyn.nocap.presetantion.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.useCase.useCaseCarritos.ActualizarCantidadUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseCarritos.CalcularTotalesUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseCarritos.EliminarItemCarritoUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseCarritos.GetCarritoUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseCarritos.LimpiarCarritoUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.Login.ObserveUserLoggedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val getCarritoUseCase: GetCarritoUseCase,
    private val eliminarItemUseCase: EliminarItemCarritoUseCase,
    private val actualizarCantidadUseCase: ActualizarCantidadUseCase,
    private val limpiarCarritoUseCase: LimpiarCarritoUseCase,
    private val observeUserLoggedUseCase : ObserveUserLoggedUseCase,
    private val calcularTotalesUseCase: CalcularTotalesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CarritoUiState())
    val state: StateFlow<CarritoUiState> = _state.asStateFlow()


    init {
        onEvent(CarritoUiEvent.LoadUsuario)
    }

    fun onEvent(event: CarritoUiEvent) {
        when (event) {
            is CarritoUiEvent.Load -> observeCarrito()
            is CarritoUiEvent.EliminarItem -> eliminarItem(event.carritoDetailsId)
            is CarritoUiEvent.ActualizarCantidad -> actualizarCantidad(
                event.carritoDetailsId,
                event.cantidad
            )
            is CarritoUiEvent.LimpiarCarrito -> limpiarCarrito()
            is CarritoUiEvent.DismissError -> dismissError()
            CarritoUiEvent.LoadUsuario -> getUsuario()
        }
    }

    fun getUsuario(){
        viewModelScope.launch {
            val user = observeUserLoggedUseCase()
            _state.update {
                it.copy(
                    isLoading =  user != null,
                    user = user
                )
            }
        }
    }

    private fun observeCarrito() {
        viewModelScope.launch {

            getCarritoUseCase(_state.value.user?.usuarioId ?: 0).collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val carrito = resource.data
                        val totales = carrito?.let { calcularTotalesUseCase(it) }

                        _state.update {
                            it.copy(
                                isLoading = false,
                                carrito = carrito,
                                subtotal = totales?.subtotal ?: 0.0,
                                envio = totales?.envio ?: 0.0,
                                total = totales?.total ?: 0.0,
                                envioGratis = totales?.envioGratis ?: false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun eliminarItem(carritoDetailsId: Int) {
        viewModelScope.launch {
            when (val result = eliminarItemUseCase(carritoDetailsId)) {
                is Resource.Error -> {
                    _state.update { it.copy(error = result.message) }
                }
                else -> { /* Success handled by flow */ }
            }
        }
    }

    private fun actualizarCantidad(carritoDetailsId: Int, cantidad: Int) {
        viewModelScope.launch {
            if (cantidad > 0) {
                when (val result = actualizarCantidadUseCase(carritoDetailsId, cantidad)) {
                    is Resource.Error -> {
                        _state.update { it.copy(error = result.message) }
                    }
                    else -> { /* Success */ }
                }
            } else {
                eliminarItem(carritoDetailsId)
            }
        }
    }

    private fun limpiarCarrito() {
        viewModelScope.launch {
            val carritoId = _state.value.carrito?.carritoId ?: return@launch
            when (val result = limpiarCarritoUseCase(carritoId)) {
                is Resource.Error -> {
                    _state.update { it.copy(error = result.message) }
                }
                else -> { /* Success */ }
            }
        }
    }


    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}

