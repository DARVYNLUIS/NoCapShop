package com.edu.darvyn.nocap.presentation.pedido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.EstadoPedido
import com.edu.darvyn.nocap.domain.repository.PedidoRepository
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.Login.ObserveUserLoggedUseCase
import com.edu.darvyn.nocap.presetantion.pedidos.PedidoUiState
import com.edu.darvyn.nocap.presetantion.pedidos.PedidosUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PedidoViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository,
    private val observeUserLoggedUseCase : ObserveUserLoggedUseCase,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidoUiState())
    val uiState: StateFlow<PedidoUiState> = _uiState.asStateFlow()

    init {
        onEvent(PedidosUiEvent.LoadPedidosByUsuario)
    }

    fun onEvent(event: PedidosUiEvent) {
        when (event) {
            is PedidosUiEvent.LoadPedidosByUsuario -> loadPedidosByUsuario()
            is PedidosUiEvent.LoadPedidoById -> loadPedidoById(event.pedidoId)
            is PedidosUiEvent.CrearPedido -> crearPedido(
                event.ordenCompraId,
                event.usuarioId,
                event.direccionEnvio
            )
            is PedidosUiEvent.ActualizarEstado -> actualizarEstado(event.pedidoId, event.nuevoEstado)
            is PedidosUiEvent.CancelarPedido -> cancelarPedido(event.pedidoId)
            is PedidosUiEvent.FiltrarPorEstado -> filtrarPorEstado(event.estado)
            PedidosUiEvent.ClearSuccess -> clearSuccess()
            PedidosUiEvent.ClearError -> clearError()
        }
    }

    private fun loadPedidosByUsuario() {
        viewModelScope.launch {
            val userLoggedUseCase = observeUserLoggedUseCase()
            pedidoRepository.observeByUsuario(userLoggedUseCase?.usuarioId ?: 0)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = Resource.Loading()
                )
                .collect { resource ->
                    _uiState.update { state ->
                        when (resource) {
                            is Resource.Loading -> state.copy(isLoading = true, error = null)
                            is Resource.Success -> state.copy(
                                pedidos = resource.data ?: emptyList(),
                                isLoading = false,
                                error = null
                            )
                            is Resource.Error -> state.copy(
                                isLoading = false,
                                error = resource.message
                            )
                        }
                    }
                }
        }
    }

    private fun loadPedidoById(pedidoId: Int) {
        viewModelScope.launch {
            pedidoRepository.observeById(pedidoId)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = Resource.Loading()
                )
                .collect { resource ->
                    _uiState.update { state ->
                        when (resource) {
                            is Resource.Loading -> state.copy(isLoading = true, error = null)
                            is Resource.Success -> state.copy(
                                pedidoSeleccionado = resource.data,
                                isLoading = false,
                                error = null
                            )
                            is Resource.Error -> state.copy(
                                isLoading = false,
                                error = resource.message
                            )
                        }
                    }
                }
        }
    }

    private fun crearPedido(ordenCompraId: Int, usuarioId: Int, direccionEnvio: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = pedidoRepository.crearPedido(ordenCompraId, usuarioId)

            _uiState.update { state ->
                when (result) {
                    is Resource.Success -> state.copy(
                        pedidoSeleccionado = result.data,
                        isLoading = false,
                        showSuccess = true,
                        successMessage = "Pedido creado exitosamente"
                    )
                    is Resource.Error -> state.copy(
                        isLoading = false,
                        error = result.message
                    )
                    is Resource.Loading -> state.copy(isLoading = true)
                }
            }
        }
    }

    private fun actualizarEstado(pedidoId: Int, nuevoEstado: EstadoPedido) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = pedidoRepository.actualizarEstado(pedidoId, nuevoEstado)

            _uiState.update { state ->
                when (result) {
                    is Resource.Success -> state.copy(
                        isLoading = false,
                        showSuccess = true,
                        successMessage = "Estado actualizado a ${nuevoEstado.estado}"
                    )
                    is Resource.Error -> state.copy(
                        isLoading = false,
                        error = result.message
                    )
                    is Resource.Loading -> state.copy(isLoading = true)
                }
            }
        }
    }

    private fun cancelarPedido(pedidoId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = pedidoRepository.cancelarPedido(pedidoId)

            _uiState.update { state ->
                when (result) {
                    is Resource.Success -> state.copy(
                        isLoading = false,
                        showSuccess = true,
                        successMessage = "Pedido cancelado exitosamente"
                    )
                    is Resource.Error -> state.copy(
                        isLoading = false,
                        error = result.message
                    )
                    is Resource.Loading -> state.copy(isLoading = true)
                }
            }
        }
    }

    private fun filtrarPorEstado(estado: EstadoPedido?) {
        viewModelScope.launch {
            if (estado == null) {
                loadPedidosByUsuario()
            } else {
                val userLoggedUseCase = observeUserLoggedUseCase()

                pedidoRepository.observeByEstado(userLoggedUseCase?.usuarioId ?: 0, estado)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5000),
                        initialValue = Resource.Loading()
                    )
                    .collect { resource ->
                        _uiState.update { state ->
                            when (resource) {
                                is Resource.Loading -> state.copy(isLoading = true, error = null)
                                is Resource.Success -> state.copy(
                                    pedidos = resource.data ?: emptyList(),
                                    isLoading = false,
                                    error = null
                                )
                                is Resource.Error -> state.copy(
                                    isLoading = false,
                                    error = resource.message
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun clearSuccess() {
        _uiState.update { it.copy(showSuccess = false, successMessage = null) }
    }

    private fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}



