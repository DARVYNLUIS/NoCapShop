package com.edu.darvyn.nocap.presetantion.productos.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.useCase.useCaseProducto.DeleteProductoByIdUseCase
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
class ListProductoViewModel @Inject constructor(
    private val observeAllProductosUseCase: ObserveAllProductoUseCase,
    private val deleteProductoByIdUseCase: DeleteProductoByIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ListProductoUiState(isLoading = true))
    val state: StateFlow<ListProductoUiState> = _state.asStateFlow()

    init {
        onEvent(ListProductoUiEvent.Load)
    }

    fun onEvent(event: ListProductoUiEvent){
        when(event) {
            is ListProductoUiEvent.Delete -> deletebyId(event.productoId)
            is ListProductoUiEvent.Edit -> _state.update { it.copy(productoId = event.id) }
            ListProductoUiEvent.Load -> observeProducto()
            is ListProductoUiEvent.ShowMessage -> _state.update {
                it.copy(message = event.message) }
        }
    }

    private fun observeProducto() {
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
                            listProducto = result.data ?: emptyList(),
                            message = null
                        )
                    }
                }
            }
        }
    }

    private fun deletebyId(id : Int){
        viewModelScope.launch {
            deleteProductoByIdUseCase(id)
        }
    }
}