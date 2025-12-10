package com.edu.darvyn.nocap.presetantion.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.edu.darvyn.nocap.domain.model.CarritoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    onNavigateBack: () -> Unit,
    goToPay: (Int?) -> Unit,
    viewModel: CarritoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.user?.usuarioId) {
        viewModel.onEvent(CarritoUiEvent.Load)

    }

    Column(modifier = Modifier.fillMaxSize()) {
        // TopBar
        TopAppBar(
            title = { Text("Mi Carrito") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, "Volver")
                }
            },
            actions = {
                if (!state.carrito?.items.isNullOrEmpty()) {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(
                            Icons.Default.Delete,
                            "Limpiar carrito",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.carrito?.items.isNullOrEmpty() -> {
                    CarritoVacio(
                        onIrTienda = onNavigateBack
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = state.carrito?.items ?: emptyList(),
                            key = { it.carritoDetailsId ?: 0 }
                        ) { item ->
                            CarritoItemCard(
                                item = item,
                                onCantidadChange = { nuevaCantidad ->
                                    item.carritoDetailsId?.let {
                                        viewModel.onEvent(
                                            CarritoUiEvent.ActualizarCantidad(state.carrito!!.carritoId, it, nuevaCantidad )
                                        )
                                    }
                                },
                                onRemove = {
                                    item.carritoDetailsId?.let {
                                        viewModel.onEvent(CarritoUiEvent.EliminarItem(it))
                                    }
                                }
                            )
                        }

                        item {
                            ResumenCarrito(
                                subtotal = state.subtotal,
                                envio = state.envio,
                                total = state.total,
                                envioGratis = state.envioGratis
                            )
                        }
                    }
                }
            }
        }

        // BottomBar
        if (!state.carrito?.items.isNullOrEmpty()) {
            Surface(
                shadowElevation = 8.dp,
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = { goToPay(state.user?.usuarioId)},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading
                    ) {
                        Text("Proceder al pago")
                    }
                }
            }
        }

        // Error Snackbar
        state.error?.let { error ->
            LaunchedEffect(error) {
                // Mostrar snackbar o dialog
                viewModel.onEvent(CarritoUiEvent.DismissError)
            }
        }
    }

    // Dialog de confirmación para limpiar carrito
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Limpiar carrito") },
            text = { Text("¿Estás seguro de que deseas eliminar todos los productos del carrito?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(CarritoUiEvent.LimpiarCarrito)
                        showClearDialog = false
                    }
                ) {
                    Text("Limpiar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarritoScreenPreview() {

    data class PreviewUsuario(val usuarioId: Int, val nombre: String)
    data class PreviewCarritoItem(
        val carritoDetailsId: Int,
        val productoNombre: String,
        val productoImagen: String,
        val precioProducto: Double,
        val cantidad: Int,
        val color: String = "",
        val talla: String = ""
    )
    data class PreviewCarrito(
        val carritoId: Int,
        val items: List<PreviewCarritoItem>
    )
    data class PreviewCarritoUiState(
        val isLoading: Boolean = false,
        val user: PreviewUsuario? = null,
        val carrito: PreviewCarrito? = null,
        val subtotal: Double = 0.0,
        val envio: Double = 0.0,
        val total: Double = 0.0,
        val envioGratis: Boolean = false,
        val error: String? = null
    )

    class PreviewCarritoViewModel {
        var state by mutableStateOf(
            PreviewCarritoUiState(
                user = PreviewUsuario(1, "Darvyn"),
                carrito = PreviewCarrito(
                    carritoId = 1,
                    items = listOf(
                        PreviewCarritoItem(
                            carritoDetailsId = 1,
                            productoNombre = "Gorra Jordan",
                            productoImagen = "https://picsum.photos/300",
                            precioProducto = 25.0,
                            cantidad = 2,
                            color = "Rojo",
                            talla = "M"
                        ),
                        PreviewCarritoItem(
                            carritoDetailsId = 2,
                            productoNombre = "Gorra Nike",
                            productoImagen = "https://picsum.photos/301",
                            precioProducto = 30.0,
                            cantidad = 1,
                            color = "Negro",
                            talla = "L"
                        )
                    )
                ),
                subtotal = 80.0,
                envio = 5.0,
                total = 85.0
            )
        )

        fun actualizarCantidad(itemId: Int, cantidad: Int) {
            val newItems = state.carrito?.items?.map {
                if (it.carritoDetailsId == itemId) it.copy(cantidad = cantidad) else it
            } ?: emptyList()
            state = state.copy(
                carrito = state.carrito?.copy(items = newItems)
            )
        }

        fun eliminarItem(itemId: Int) {
            val newItems = state.carrito?.items?.filterNot { it.carritoDetailsId == itemId } ?: emptyList()
            state = state.copy(
                carrito = state.carrito?.copy(items = newItems)
            )
        }

        fun limpiarCarrito() {
            state = state.copy(
                carrito = state.carrito?.copy(items = emptyList())
            )
        }
    }

    val fakeViewModel = remember { PreviewCarritoViewModel() }

    @Composable
    fun CarritoScreenForPreview(
        onNavigateBack: () -> Unit,
        goToPay: (Int?) -> Unit,
        viewModel: PreviewCarritoViewModel
    ) {
        val state = viewModel.state

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    if (!state.carrito?.items.isNullOrEmpty()) {
                        IconButton(onClick = { viewModel.limpiarCarrito() }) {
                            Icon(
                                Icons.Default.Delete,
                                "Limpiar carrito",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    state.isLoading -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                    state.carrito?.items.isNullOrEmpty() -> CarritoVacio(onIrTienda = onNavigateBack)
                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.carrito!!.items, key = { it.carritoDetailsId }) { item ->
                            CarritoItemCard(
                                item = com.edu.darvyn.nocap.domain.model.CarritoItem(
                                    carritoDetailsId = item.carritoDetailsId,
                                    productoId = 0,
                                    productoNombre = item.productoNombre,
                                    productoImagen = item.productoImagen,
                                    precioProducto = item.precioProducto,
                                    cantidad = item.cantidad,
                                    color = item.color,
                                    talla = item.talla
                                ),
                                onCantidadChange = { nuevaCantidad ->
                                    viewModel.actualizarCantidad(item.carritoDetailsId, nuevaCantidad)
                                },
                                onRemove = {
                                    viewModel.eliminarItem(item.carritoDetailsId)
                                }
                            )
                        }

                        item {
                            ResumenCarrito(
                                subtotal = state.subtotal,
                                envio = state.envio,
                                total = state.total,
                                envioGratis = state.envioGratis
                            )
                        }
                    }
                }
            }

            if (!state.carrito?.items.isNullOrEmpty()) {
                Surface(
                    shadowElevation = 8.dp,
                    tonalElevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Button(
                            onClick = { goToPay(state.user?.usuarioId) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !state.isLoading
                        ) {
                            Text("Proceder al pago")
                        }
                    }
                }
            }
        }
    }

    MaterialTheme {
        CarritoScreenForPreview(
            onNavigateBack = {},
            goToPay = {},
            viewModel = fakeViewModel
        )
    }
}
