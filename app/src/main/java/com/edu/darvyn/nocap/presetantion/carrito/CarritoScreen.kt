package com.edu.darvyn.nocap.presetantion.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

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