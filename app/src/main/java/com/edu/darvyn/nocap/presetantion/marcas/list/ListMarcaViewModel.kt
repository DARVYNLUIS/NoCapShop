package com.edu.darvyn.nocap.presetantion.marcas.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.useCase.useCaseMarcas.DeleteMarcaByIdUseCase
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
class ListMarcaViewModel @Inject constructor(
    private val observeAllMarcasUseCase: ObserveAllMarcasUseCase,
    private val deleteMarcaByIdUseCase: DeleteMarcaByIdUseCase
) : ViewModel(){
    private val _state = MutableStateFlow(ListMarcaUiState(isLoading = true))
    val state: StateFlow<ListMarcaUiState> = _state.asStateFlow()

    init {
        onEvent(ListMarcasUiEvent.Load)

    }

    fun onEvent(event: ListMarcasUiEvent){
        when(event) {
            is ListMarcasUiEvent.Delete -> deletebyId(event.marcaId)
            is ListMarcasUiEvent.Edit -> _state.update { it.copy(marcaId = event.id)}
            ListMarcasUiEvent.Load -> observeMarca()
            is ListMarcasUiEvent.ShowMessage -> TODO()
        }
    }

    private fun deletebyId(id : Int){
        viewModelScope.launch {
            deleteMarcaByIdUseCase(id)
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
                            listMarcas = result.data ?: emptyList(),
                            message = null
                        )
                    }
                }
            }
        }
    }
}