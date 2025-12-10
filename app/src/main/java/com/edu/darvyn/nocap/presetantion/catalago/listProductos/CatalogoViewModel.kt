package com.edu.darvyn.nocap.presetantion.catalago.listProductos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.useCase.useCaseMarcas.ObserveAllMarcasUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseProducto.ObserveAllProductoUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseProducto.getProductoByMarcaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class CatalogoViewModel @Inject constructor(
    private val observeAllProductosUseCase: ObserveAllProductoUseCase,
    private val observeAllMarcasUseCase: ObserveAllMarcasUseCase,
    private val observeProductoByMarcaUseCase: getProductoByMarcaUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CatalogoUiState(isLoading = true))
    open val state: StateFlow<CatalogoUiState> = _state.asStateFlow()

    init {
        onEvent(ListProductosUiEvent.LoadProductos)
        onEvent(ListProductosUiEvent.LoadMarcas)
    }

    fun onEvent(event: ListProductosUiEvent) {
        when (event) {
            ListProductosUiEvent.LoadProductos -> observeProductos()
            is ListProductosUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
            ListProductosUiEvent.LoadMarcas -> observeMarca()
            is ListProductosUiEvent.SeleccionarMarcar -> toggleMarcaSelection(event.marcaId)
            ListProductosUiEvent.AplicarFiltro -> aplicarFiltro()
            ListProductosUiEvent.LimpiarFiltro -> limpiarFiltro()
            is ListProductosUiEvent.ModalOn -> _state.update { it.copy(mostrarModal = event.state) }
        }

    }

    fun limpiarFiltro() {
        _state.update { it.copy(selectedMarcaIds = emptyList()) }
        observeProductos()
    }

    fun aplicarFiltro() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = observeProductoByMarcaUseCase(state.value.selectedMarcaIds)

            when (result) {
                is Resource.Success -> _state.update {
                    it.copy(
                        productos = result.data ?: emptyList(),
                        message = null,
                        isLoading = false
                    )
                }

                is Resource.Error -> _state.update {
                    it.copy(
                        message = result.message ?: "Error desconocido",
                        isLoading = false
                    )
                }

                is Resource.Loading -> _state.update { it.copy(isLoading = true) }
            }
        }
    }

    fun toggleMarcaSelection(marcaId: Int) {
        _state.update {
            val newSelection = if (it.selectedMarcaIds!!.contains(marcaId)) {
                it.selectedMarcaIds - marcaId
            } else {
                it.selectedMarcaIds + marcaId
            }
            it.copy(selectedMarcaIds = newSelection)
        }
    }

    private fun observeProductos() {
        viewModelScope.launch {
            observeAllProductosUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            message = result.message ?: "Error desconocido"
                        )
                    }

                    is Resource.Loading -> _state.update {
                        it.copy(isLoading = true)
                    }

                    is Resource.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            productos = result.data ?: emptyList(),
                            message = null
                        )
                    }
                }
            }
        }
    }

    private fun observeMarca() {
        viewModelScope.launch {
            observeAllMarcasUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                            message = result.message ?: "Error desconocido"
                        )
                    }

                    is Resource.Loading -> _state.update {
                        it.copy(isLoading = true)
                    }

                    is Resource.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            marcas = result.data ?: emptyList(),
                            message = null
                        )
                    }
                }
            }
        }

    }


}