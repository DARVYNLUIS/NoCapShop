package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelCategorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
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
class PanelCaterogriasViewModel @Inject constructor(
    private val observeAllCategoriaUseCase: ObserveAllCategoriaUseCase,

) : ViewModel(){
    private val _state = MutableStateFlow(PanelCategoriasUiState(isLoading = true))
    val state: StateFlow<PanelCategoriasUiState> = _state.asStateFlow()

    init {
        onEvent(PanelCategoriasUiEvent.Load)
    }

    fun onEvent(event: PanelCategoriasUiEvent){
        when(event) {
            PanelCategoriasUiEvent.Load -> observeCategoria()
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
                            categorias = result.data ?: emptyList(),
                            message = null
                        )
                    }
                }
            }
        }

    }

}