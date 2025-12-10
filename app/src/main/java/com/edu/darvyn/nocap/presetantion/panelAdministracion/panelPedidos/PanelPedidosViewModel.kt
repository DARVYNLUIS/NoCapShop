package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelPedidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.domain.useCase.useCasePedidos.GetAllPedidosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PanelPedidosViewModel @Inject constructor(
    private val getAllPedidosUseCase: GetAllPedidosUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(PanelPedidosUiState(isLoading = true))
    val state: StateFlow<PanelPedidosUiState> = _state.asStateFlow()

    init {
        onEvent(PanelPedidosUiEvent.Load)
    }

    fun onEvent(event: PanelPedidosUiEvent) {
        when (event) {
            PanelPedidosUiEvent.Load -> observePedidos()
        }
    }

    private fun observePedidos() {
        viewModelScope.launch {
            val pedidos = getAllPedidosUseCase()
           _state.update {
             it.copy(
                 pedidos = pedidos
             )
           }
        }
    }
}