package com.edu.darvyn.nocap.presetantion.catalago.observeProducto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Producto
import com.edu.darvyn.nocap.domain.useCase.useCaseCarritos.AgregarProductoUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseProducto.ObserveProductoByIdUseCase
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.Login.ObserveUserLoggedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObserveProductoViewModel @Inject constructor(
    private val observeProductoByIdUseCase: ObserveProductoByIdUseCase,
    private val agregarProductoAlCarritoUseCase: AgregarProductoUseCase,
    private val observeUserLoggedUseCase : ObserveUserLoggedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductoDetalleUiState())
    val state: StateFlow<ProductoDetalleUiState> = _state.asStateFlow()

    init {
        onEvent(ProductoDetalleUiEvent.LoadUsuario)
    }

    private fun loadProducto(productoId: Int) {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val productoEncontrado = observeProductoByIdUseCase(productoId)

                if (productoEncontrado.productoId == null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Producto no encontrado"
                        )
                    }
                    return@launch
                }

                _state.update {
                    it.copy(
                        producto = productoEncontrado,
                        productoId = productoEncontrado.productoId,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error inesperado"
                    )
                }
            }
        }
    }

    fun onEvent(event: ProductoDetalleUiEvent) {
        when (event) {
            is ProductoDetalleUiEvent.SelectTalla -> {
                _state.value = _state.value.copy(selectedTalla = event.talla)
            }
            is ProductoDetalleUiEvent.SelectColor -> {
                _state.value = _state.value.copy(selectedColor = event.color)
            }
            is ProductoDetalleUiEvent.ChangeCantidad -> {
                _state.value = _state.value.copy(cantidad = event.cantidad)
            }
            is ProductoDetalleUiEvent.AgregarAlCarrito -> {
                agregarAlCarrito()
            }

           is  ProductoDetalleUiEvent.LoadProducto -> {
               loadProducto(event.productoId)
           }

            ProductoDetalleUiEvent.LoadUsuario -> getUsuario()
        }
    }

    fun getUsuario(){
        viewModelScope.launch {
            val user = observeUserLoggedUseCase()
            _state.update {
                it.copy(
                    isLoading =  user != null,
                    user = user
                )
            }
        }
    }

    private fun agregarAlCarrito() {
        val currentState = _state.value
        val producto = currentState.producto
        val talla = currentState.selectedTalla
        val color = currentState.selectedColor
        val cantidad = currentState.cantidad

        // Validaciones de UI
        if (producto == null) {
            _state.update { it.copy(error = "Producto no cargado") }
            return
        }

        if (talla.isNullOrBlank()) {
            _state.update { it.copy(error = "Selecciona una talla") }
            return
        }

        if (color.isNullOrBlank()) {
            _state.update { it.copy(error = "Selecciona un color") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val result = agregarProductoAlCarritoUseCase(
                usuarioId = _state.value.user?.usuarioId ?: 0 ,
                productoId = producto.productoId!!,
                cantidad = cantidad,
                precio = producto.precioVenta,
                color = color,
                talla = talla
            )

            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            mensajeExito = result.data.toString() ?: "Producto agregado al carrito",
                            selectedTalla = null,
                            selectedColor = null,
                            cantidad = 1,
                            error = null,
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error al agregar al carrito"
                        )
                    }
                }

                is Resource.Loading -> {
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ObserveProductoScreenPreview() {
    val productoEjemplo = Producto(
        productoId = 1,
        nombre = "Gorra Jordan",
        descripcion = "Gorra deportiva Jordan, ligera y cómoda",
        fechaCreacion = "2025-12-10",
        productoImagen = "https://picsum.photos/300",
        precioVenta = 25.0,
        stocks = 10,
        categoriaId = 1,
        marcaId = 2,
        activo = true,
        listaTamanos = listOf("S", "M", "L"),
        listaColores = listOf("Rojo", "Negro")
    )

    var state by remember {
        mutableStateOf(
            ProductoDetalleUiState(
                productoId = productoEjemplo.productoId,
                producto = productoEjemplo,
                isLoading = false,
                selectedTalla = "M",
                selectedColor = "Rojo",
                cantidad = 1
            )
        )
    }

    val fakeViewModel = object {
        fun onEvent(event: ProductoDetalleUiEvent) {
            state = when (event) {
                is ProductoDetalleUiEvent.SelectTalla -> state.copy(selectedTalla = event.talla)
                is ProductoDetalleUiEvent.SelectColor -> state.copy(selectedColor = event.color)
                is ProductoDetalleUiEvent.ChangeCantidad -> state.copy(cantidad = event.cantidad)
                else -> state
            }
        }
    }

    ObserveProductoScreenPreviewContent(
        state = state,
        onEvent = fakeViewModel::onEvent,
        onNavigateBack = {}
    )
}

@Composable
fun ObserveProductoScreenPreviewContent(
    state: ProductoDetalleUiState,
    onEvent: (ProductoDetalleUiEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    ProductoDetailContent(
        producto = state.producto!!,
        selectedTalla = state.selectedTalla,
        selectedColor = state.selectedColor,
        cantidad = state.cantidad,
        onTallaSelected = { onEvent(ProductoDetalleUiEvent.SelectTalla(it)) },
        onColorSelected = { onEvent(ProductoDetalleUiEvent.SelectColor(it)) },
        onCantidadChanged = { onEvent(ProductoDetalleUiEvent.ChangeCantidad(it)) },
        onAgregarAlCarrito = {}
    )
}
