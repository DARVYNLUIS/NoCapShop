package com.edu.darvyn.nocap.presetantion.categoria.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.domain.model.Categoria
import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.ObserveByIdUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.SaveCategoriaUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.validateDescripcionCategoria
import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.validateNombreCategoria
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCategoriaViewModel @Inject constructor(
    private val observeByIdUseCase: ObserveByIdUseCase,
    private val saveCategoriaUseCase: SaveCategoriaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditCategoriaUiState())
    val state: StateFlow<EditCategoriaUiState> = _state.asStateFlow()

    fun onEvent(event: EditCategoriaUiEvent) {
        when (event) {
            is EditCategoriaUiEvent.CategoriaDescripcionChange -> _state.update {
                it.copy(categoriaDescripcion = event.descripcion, errorCategoriaDescripcion = null)
            }

            is EditCategoriaUiEvent.CategoriaNombreChange -> _state.update {
                it.copy(categoriaNombre = event.nombre, errorCategoriaNombre = null)
            }

            EditCategoriaUiEvent.Save -> saveCategoria()
            is EditCategoriaUiEvent.LoadCategoria -> observeById(event.id)
        }
    }

    private fun observeById(id: Int) {
        if (id == 0) {
            _state.update {
                it.copy(
                    isNew = true,
                    categoriaId = null
                )
            }
            return
        }
        viewModelScope.launch {
            val categoria = observeByIdUseCase(id)
            if (categoria.categoriaId != 0) {
                _state.update {
                    it.copy(
                        isNew = false,
                        categoriaId = categoria.categoriaId,
                        categoriaNombre = categoria.nombre,
                        categoriaDescripcion = categoria.descripcion
                    )
                }

            }
        }
    }

    private fun saveCategoria() {
        viewModelScope.launch {
            if (validar()) {
                _state.update { it.copy(isSaving = true) }
                val result = saveCategoriaUseCase(_state.value.toDomain())
                result.onSuccess {
                    _state.update {
                        it.copy(
                            isSaving = false,
                            saved = true,
                            categoriaId = 0
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
            validateNombreCategoria(_state.value.categoriaNombre.toString())
        var descripcionCorrecto =
            validateDescripcionCategoria(_state.value.categoriaDescripcion.toString())

        _state.update {
            it.copy(
                errorCategoriaNombre = nombreCorrecto.error,
                errorCategoriaDescripcion = descripcionCorrecto.error
            )
        }

        return descripcionCorrecto.isValid == true && nombreCorrecto.isValid == true
    }

    fun EditCategoriaUiState.toDomain() = Categoria(
        categoriaId = _state.value.categoriaId ?: 0,
        nombre = _state.value.categoriaNombre ?: "",
        descripcion = _state.value.categoriaDescripcion ?: "",
        activa = this.activa == true
    )
}
