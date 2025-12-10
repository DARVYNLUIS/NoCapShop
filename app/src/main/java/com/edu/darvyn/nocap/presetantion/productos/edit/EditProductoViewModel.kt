package com.edu.darvyn.nocap.presetantion.productos.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.local.entities.obtenerFechaActual
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Producto
import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.ObserveAllCategoriaUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.ValidationResult
import com.edu.darvyn.nocap.domain.useCase.useCaseMarcas.ObserveAllMarcasUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseProducto.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditProductoViewModel @Inject constructor(
    private val observeProductoByIdUseCase: ObserveProductoByIdUseCase,
    private val saveProductoUseCase: SaveProductoUseCase,
    private val observeCategoriasUseCase: ObserveAllCategoriaUseCase,
    private val observeMarcasUseCase: ObserveAllMarcasUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(EditProductoUiState())
    val state: StateFlow<EditProductoUiState> = _state.asStateFlow()

    init {
        onEvent(EditProductoUiEvent.LoadMarcas)
        onEvent(EditProductoUiEvent.LoadCategorias)
    }


    fun onEvent(event: EditProductoUiEvent) {
        when (event) {
            is EditProductoUiEvent.LoadProducto -> observeById(event.id)
            is EditProductoUiEvent.NombreChange -> _state.update {
                it.copy(
                    nombre = event.nombre,
                    nombreError = null
                )
            }

            is EditProductoUiEvent.DescripcionChange -> _state.update {
                it.copy(
                    descripcion = event.descripcion,
                    descripcionError = null
                )
            }

            is EditProductoUiEvent.PrecioCompraChange -> _state.update {
                it.copy(
                    precioCompra = event.precioCompra,
                    precioCompraError = null
                )
            }

            is EditProductoUiEvent.PrecioVentaChange -> _state.update {
                it.copy(
                    precioVenta = event.precio,
                    precioVentaError = null
                )
            }

            is EditProductoUiEvent.ImagenChange -> _state.update {
                it.copy(
                    productoImagen = event.url,
                    productoImagenError = null
                )
            }

            is EditProductoUiEvent.StockChange -> _state.update {
                it.copy(
                    stocks = event.stock,
                    stocksError = null
                )
            }

            is EditProductoUiEvent.CategoriaChange -> _state.update {
                it.copy(
                    categoriaId = event.categoriaId,
                    categoriaError = null
                )
            }

            is EditProductoUiEvent.MarcaChange -> _state.update {
                it.copy(
                    marcaId = event.marcaId,
                    marcaError = null
                )
            }

            EditProductoUiEvent.LoadCategorias -> observeCategorias()
            EditProductoUiEvent.LoadMarcas -> observeMarcas()
            EditProductoUiEvent.Save -> saveProducto()
            is EditProductoUiEvent.ToggleColor -> agregarColor(event.color)

            is EditProductoUiEvent.ToggleTamano -> agregarTalla(event.tamano)
        }
    }

    private fun agregarTalla(talla: String) {
        if (_state.value.listaTamano.contains(talla)) {
            _state.update {
                it.copy(
                    listaTamano = _state.value.listaTamano - talla
                )
            }
        } else {
            _state.update {
                it.copy(
                    listaTamano = _state.value.listaTamano + talla
                )
            }

        }
    }

    private fun agregarColor(colors: String) {
        if (_state.value.listaColores.contains(colors)) {
            _state.update {
                it.copy(
                    listaColores = _state.value.listaColores - colors
                )
            }
        } else {
            _state.update {
                it.copy(
                    listaColores = _state.value.listaColores + colors
                )
            }

        }

    }

    private fun observeById(id: Int) {
        if (id == 0) {
            _state.update {
                it.copy(
                    isNew = true,
                    productoId = null
                )
            }
            return
        }
        viewModelScope.launch {
            val producto = observeProductoByIdUseCase(id)
            if (producto.productoId != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        productoId = producto.productoId,
                        nombre = producto.nombre,
                        descripcion = producto.descripcion,
                        productoImagen = producto.productoImagen,
                        precioVenta = producto.precioVenta.toString(),
                        stocks = producto.stocks.toString(),
                        categoriaId = producto.categoriaId,
                        marcaId = producto.marcaId,
                        listaTamano = producto.listaTamanos,
                        listaColores = producto.listaColores
                    )
                }
            }
        }
    }

    private fun saveProducto() {
        viewModelScope.launch {
            if (true) {
                _state.update { it.copy(isSaving = true) }
                val result = saveProductoUseCase(state.value.toDomain())
                result.onSuccess {
                    _state.update { it.copy(isSaving = false, saved = true, productoId = 0) }
                }.onFailure {
                    _state.update { it.copy(isSaving = false) }
                }
            }
        }
    }

    private fun validar(): Boolean {
        val current = _state.value

        val nombreResult = validateNombreProducto(current.nombre ?: "")
        val descripcionResult = validateDescripcionProducto(current.descripcion ?: "")
        val precioVenta = current.precioVenta?.toDoubleOrNull() ?: -1.0
        val precioVentaResult = validatePrecioVenta(precioVenta)
        val stock = current.stocks?.toIntOrNull() ?: -1
        val stockResult = if (stock < 0) ValidationResult(
            false,
            "El stock debe ser mayor o igual a 0"
        ) else ValidationResult(true)
        val categoriaResult = validateCategoriaId(current.categoriaId ?: 0)
        val marcaResult = validateMarcaId(current.marcaId ?: 0)

        _state.update {
            it.copy(
                nombreError = nombreResult.error,
                descripcionError = descripcionResult.error,
                precioVentaError = precioVentaResult.error,
                stocksError = stockResult.error,
                categoriaError = categoriaResult.error,
                marcaError = marcaResult.error
            )
        }

        return nombreResult.isValid &&
                descripcionResult.isValid &&
                precioVentaResult.isValid &&
                stockResult.isValid &&
                categoriaResult.isValid &&
                marcaResult.isValid
    }

    private fun observeMarcas() {
        viewModelScope.launch {
            observeMarcasUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }

                    is Resource.Loading -> _state.update {
                        it.copy(isLoading = true)
                    }

                    is Resource.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            listaMarcas = result.data ?: emptyList(),
                        )
                    }
                }
            }
        }
    }

    private fun observeCategorias() {
        viewModelScope.launch {
            observeCategoriasUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Error -> _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }

                    is Resource.Loading -> _state.update {
                        it.copy(isLoading = true)
                    }

                    is Resource.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            listaCategoria = result.data ?: emptyList(),
                        )
                    }
                }
            }
        }
    }

    private fun EditProductoUiState.toDomain() = Producto(
        productoId = this.productoId,
        nombre = this.nombre ?: "",
        descripcion = this.descripcion ?: "",
        fechaCreacion =  obtenerFechaActual(),
        productoImagen = this.productoImagen ?: "",
        precioVenta = this.precioVenta?.toDoubleOrNull() ?: 0.0,
        stocks = this.stocks?.toIntOrNull() ?: 0,
        categoriaId = this.categoriaId ?: 0,
        marcaId = this.marcaId ?: 0,
        listaTamanos = this.listaTamano,
        listaColores = this.listaColores,
        activo = true
    )
}
