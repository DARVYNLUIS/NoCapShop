package com.edu.darvyn.nocap.presetantion.marcas.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.domain.model.Marcas
import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.validateNombreCategoria
import com.edu.darvyn.nocap.domain.useCase.useCaseMarcas.ObserveMarcaByIdUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseMarcas.SaveMarcaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMarcaViewModel @Inject constructor(
    private val observeMarcaByIdUseCase: ObserveMarcaByIdUseCase,
    private val saveMarcaUseCase: SaveMarcaUseCase,

): ViewModel(){
    private val _state = MutableStateFlow(EditMarcaUiState())
    val state: StateFlow<EditMarcaUiState> = _state.asStateFlow()

    fun onEvent(event: EditMarcaUiEvent) {
        when (event) {
            is EditMarcaUiEvent.MarcaNombreChange -> _state.update {
                it.copy(marcaNombre = event.nombre, errorMarcaNombre = null)
            }
            is EditMarcaUiEvent.LoadMarca -> observeById(event.id)
            EditMarcaUiEvent.Save -> saveCategoria()
        }
    }

    private fun observeById(id: Int) {
        if (id == 0) {
            _state.update {
                it.copy(
                    isNew = true,
                    marcaId = null
                )
            }
            return
        }
        viewModelScope.launch {
            val marca = observeMarcaByIdUseCase(id)
            if (marca.marcaId != 0) {
                _state.update {
                    it.copy(
                        isNew = false,
                        marcaId = marca.marcaId,
                        marcaNombre = marca.nombre,
                    )
                }

            }
        }
    }

    private fun saveCategoria() {
        viewModelScope.launch {
            if (validar()) {
                _state.update { it.copy(isSaving = true) }
                val result = saveMarcaUseCase(_state.value.toDomain())
                result.onSuccess {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            saved = true,
                            marcaId = 0
                        )
                    }
                }.onFailure { e ->
                    _state.update { it.copy(isSaving = false) }
                }
            }
        }
    }

    private fun validar(): Boolean {
        var nombreCorrecto =
            validateNombreCategoria(_state.value.marcaNombre.toString())

        _state.update {
            it.copy(
                errorMarcaNombre = nombreCorrecto.error,
            )
        }

        return nombreCorrecto.isValid == true
    }

    fun EditMarcaUiState.toDomain() = Marcas(
        marcaId = _state.value.marcaId ?: 0,
        nombre = _state.value.marcaNombre ?: "",
        activa = this.activa ?: false
    )
}