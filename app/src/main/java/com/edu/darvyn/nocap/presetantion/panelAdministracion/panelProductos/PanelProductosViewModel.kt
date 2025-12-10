package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelProductos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.useCase.useCaseProducto.ObserveAllProductoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PanelProductosViewModel @Inject constructor(
    private val observeAllProductosUseCase: ObserveAllProductoUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(PanelProductosUiState(isLoading = true))
    val state: StateFlow<PanelProductosUiState> = _state.asStateFlow()

    init {
        onEvent(PanelProductosUiEvent.Load)
    }

    fun onEvent(event: PanelProductosUiEvent){
        when(event) {
            PanelProductosUiEvent.Load -> observeProductos()
        }
    }

    private fun observeProductos() {
        viewModelScope.launch{
            observeAllProductosUseCase().collectLatest{ result ->
                when(result) {
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
}