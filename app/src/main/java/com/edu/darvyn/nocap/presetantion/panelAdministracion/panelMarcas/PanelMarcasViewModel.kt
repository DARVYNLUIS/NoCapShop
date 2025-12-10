package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelMarcas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.useCase.useCaseMarcas.ObserveAllMarcasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PanelMarcasViewModel @Inject constructor(
    private val observeAllMarcasUseCase: ObserveAllMarcasUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(PanelMarcasUiState(isLoading = true))
    open val state: StateFlow<PanelMarcasUiState> = _state.asStateFlow()

    init {
        onEvent(PanelMarcasUiEvent.Load)

    }

    open fun onEvent(event: PanelMarcasUiEvent){
        when(event) {
            PanelMarcasUiEvent.Load -> observeMarca()
        }
    }

    private fun observeMarca() {
        viewModelScope.launch{
            observeAllMarcasUseCase().collectLatest{ result ->
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
                            marcas = result.data ?: emptyList(),
                            message = null
                        )
                    }
                }
            }
        }

    }
}