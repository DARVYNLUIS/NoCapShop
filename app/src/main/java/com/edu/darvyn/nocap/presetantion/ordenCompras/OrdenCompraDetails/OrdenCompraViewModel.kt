package com.edu.darvyn.nocap.presetantion.ordenCompras.OrdenCompraDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.repository.CarritoRepository
import com.edu.darvyn.nocap.domain.repository.OrdenCompraRepository
import com.edu.darvyn.nocap.domain.useCase.useCaseOrdenCompra.CrearOrdenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdenCompraViewModel @Inject constructor(
    private val carritoRepository: CarritoRepository,
    private val ordenCompraRepository: OrdenCompraRepository,
    private val crearOrdenUseCase: CrearOrdenUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(OrdenCompraUiState())
    val state: StateFlow<OrdenCompraUiState> = _state.asStateFlow()

    var currentCarritoId = 0
    fun onEvent(event: OrdenCompraUiEvent) {
        when (event) {
            is OrdenCompraUiEvent.Load -> loadCheckout(event.carritoId)
            is OrdenCompraUiEvent.PagarAhora -> crearOrden(pagarAhora = true, event.usuarioId)
            is OrdenCompraUiEvent.PagarDespues -> crearOrden(pagarAhora = false,event.usuarioId)
            is OrdenCompraUiEvent.DismissConfirmation -> dismissConfirmation()
            is OrdenCompraUiEvent.DismissError -> dismissError()
            is OrdenCompraUiEvent.VolverAInicio -> {}
            is OrdenCompraUiEvent.LoadOrden -> loadOrden(event.ordenId)
        }
    }

    private fun loadOrden(ordenId: Int?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val orden = ordenCompraRepository.getById(ordenId!!)

            if (orden == null) {
                _state.update { it.copy(isLoading = false, error = "Orden no encontrada") }
                return@launch
            }

            val carrito = carritoRepository.getById(orden.carritoId)

            if (carrito == null) {
                _state.update { it.copy(isLoading = false, error = "Carrito no asociado!") }
                return@launch
            }
            currentCarritoId = carrito.carritoId!!
            val subtotal = carrito.items.sumOf { it.subtotal }
            val itbis = subtotal * 0.18
            val envioGratis = subtotal >= 30.0
            val envio = if (envioGratis) 0.0 else 5.0
            val total = subtotal + itbis + envio

            _state.update {
                it.copy(
                    carrito = carrito,
                    subtotal = subtotal,
                    itbis = itbis,
                    envio = envio,
                    total = total,
                    envioGratis = envioGratis,
                    ordenCreada = orden,
                    isLoading = false
                )
            }
        }
    }

    private fun loadCheckout(usuarioId: Int?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            carritoRepository.observeCarrito(usuarioId!!).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val carrito = resource.data
                        currentCarritoId = carrito?.carritoId ?: 0

                        if (carrito != null && carrito.items.isNotEmpty()) {
                            val subtotal = carrito.items.sumOf { it.subtotal }
                            val itbis = subtotal * 0.18  // 18% ITBIS
                            val envioGratis = subtotal >= 30.0
                            val envio = if (envioGratis) 0.0 else 5.0
                            val total = subtotal + itbis + envio

                            // Contar pagos pendientes
                            val pagosPendientes = ordenCompraRepository.countPagosPendientes(usuarioId)

                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    carrito = carrito,
                                    subtotal = subtotal,
                                    itbis = itbis,
                                    envio = envio,
                                    total = total,
                                    envioGratis = envioGratis,
                                    pagosPendientes = pagosPendientes,
                                    error = null
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = "El carrito está vacío"
                                )
                            }
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
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun crearOrden(pagarAhora: Boolean, usuarioId: Int?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = crearOrdenUseCase(
                carritoId = currentCarritoId,
                usuarioId = usuarioId!!,
                pagarAhora = pagarAhora
            )) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            ordenCreada = result.data,
                            showConfirmation = true,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error al crear orden"
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun dismissConfirmation() {
        _state.update { it.copy(showConfirmation = false) }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }


}


