package com.edu.darvyn.nocap.presetantion.categoria.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.DeleteCategoriaUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.ObserveAllCategoriaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCategoriaViewModel @Inject constructor(
    private val observeAllCategoriaUseCase: ObserveAllCategoriaUseCase,
    private val deleteCategoriaUseCase: DeleteCategoriaUseCase
): ViewModel() {
    private val _state = MutableStateFlow(ListCategoriaUiState(isLoading = true))
    val state: StateFlow<ListCategoriaUiState> = _state.asStateFlow()

    init {
        onEvent(ListCategoriaUiEvent.Load)

    }

    fun onEvent(event: ListCategoriaUiEvent){
        when(event) {
            is ListCategoriaUiEvent.Delete -> deletebyId(event.categoriaId)
            is ListCategoriaUiEvent.Edit -> _state.update { it.copy(categoriaId = event.id)}
            ListCategoriaUiEvent.Load -> observeCategoria()
            is ListCategoriaUiEvent.ShowMessage ->_state.update { it.copy(message = event.message) }
        }
    }

    private fun deletebyId(id : Int){
        viewModelScope.launch {
            deleteCategoriaUseCase(id)
        }
    }

    private fun observeCategoria() {
        viewModelScope.launch{
            observeAllCategoriaUseCase().collectLatest{ result ->
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
                            listCategorias = result.data ?: emptyList(),
                            message = null
                        )
                    }
                }
            }
        }

    }
}