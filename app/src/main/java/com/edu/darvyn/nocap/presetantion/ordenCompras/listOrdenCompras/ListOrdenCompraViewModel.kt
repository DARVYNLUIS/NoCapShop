package com.edu.darvyn.nocap.presetantion.ordenCompras.listOrdenCompras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.OrdenCompra
import com.edu.darvyn.nocap.domain.useCase.useCaseOrdenCompra.ObserveOrdenesByUsuarioUseCae
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListOrdenCompraViewModel @Inject constructor(
    private val observarOrdenesByUsuarios: ObserveOrdenesByUsuarioUseCae
) : ViewModel() {

    private val _state = MutableStateFlow(OrdenesListUiState())
    val state: StateFlow<OrdenesListUiState> = _state.asStateFlow()

    private var ordenesCompletas: List<OrdenCompra>? = emptyList()

    fun onEvent(event: OrdenesListUiEvent) {
        when (event) {
            is OrdenesListUiEvent.LoadOrdenes -> loadOrdenCompra(event.usuarioId)
            is OrdenesListUiEvent.FiltrarPorEstado -> filtrarOrdenes(event.filtro)
        }
    }

    fun loadOrdenCompra(usuarioId: Int) {
        viewModelScope.launch {
            observarOrdenesByUsuarios(usuarioId).collect { result ->

                when(result) {

                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }

                    is Resource.Success -> {
                        ordenesCompletas = result.data
                        _state.value = state.value.copy(
                            ordenes = result.data,
                            isLoading = false,
                            error = null
                        )
                    }

                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            isLoading = false,
                            error = result.message ?: "Error desconocido"
                        )
                    }
                }
            }
        }
    }
    private fun filtrarOrdenes(filtro: FiltroEstado) {
        val ordenesFiltradas = when (filtro) {
            FiltroEstado.TODOS -> ordenesCompletas
            FiltroEstado.PAGADO -> ordenesCompletas?.filter { it.pagado }
            FiltroEstado.PENDIENTE -> ordenesCompletas?.filter { !it.pagado }
        }

        _state.update {
            it.copy(
                ordenes = ordenesFiltradas,
                filtroEstado = filtro
            )
        }
    }
}