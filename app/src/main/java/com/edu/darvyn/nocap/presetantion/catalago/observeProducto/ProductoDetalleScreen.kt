package com.edu.darvyn.nocap.presetantion.catalago.observeProducto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.edu.darvyn.nocap.domain.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObserveProductoScreen(
    productoId: Int,
    viewModel: ObserveProductoViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    LaunchedEffect(productoId) {
        if (productoId != 0) {
            viewModel.onEvent(ProductoDetalleUiEvent.LoadProducto(productoId))
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TopBar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        "Detalle del Producto",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Contenido
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = state.error ?: "Error desconocido",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Button(onClick = {
                                viewModel.onEvent(ProductoDetalleUiEvent.LoadProducto(productoId))
                            }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                state.producto != null -> {
                    ProductoDetailContent(
                        producto = state.producto!!,
                        selectedTalla = state.selectedTalla,
                        selectedColor = state.selectedColor,
                        cantidad = state.cantidad,
                        onTallaSelected = { viewModel.onEvent(ProductoDetalleUiEvent.SelectTalla(it)) },
                        onColorSelected = { viewModel.onEvent(ProductoDetalleUiEvent.SelectColor(it)) },
                        onCantidadChanged = { viewModel.onEvent(ProductoDetalleUiEvent.ChangeCantidad(it)) },
                        onAgregarAlCarrito = { viewModel.onEvent(ProductoDetalleUiEvent.AgregarAlCarrito) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetailContent(
    producto: Producto,
    selectedTalla: String?,
    selectedColor: String?,
    cantidad: Int,
    onTallaSelected: (String) -> Unit,
    onColorSelected: (String) -> Unit,
    onCantidadChanged: (Int) -> Unit,
    onAgregarAlCarrito: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Imagen del producto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = producto.productoImagen,
                contentDescription = producto.nombre,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Información del producto
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nombre y precio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "$${String.format("%.2f", producto.precioVenta)}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Stock badge
            Surface(
                color = if (producto.stocks > 0)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Inventory,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (producto.stocks > 0)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = if (producto.stocks > 0) "${producto.stocks} disponibles" else "Agotado",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (producto.stocks > 0)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            HorizontalDivider()

            // Descripción
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider()

            // Talla
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Talla",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    producto.listaTamanos.forEach { talla ->
                        FilterChip(
                            selected = selectedTalla == talla,
                            onClick = { onTallaSelected(talla) },
                            label = { Text(talla) }
                        )
                    }
                }
            }

            // Color
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Color",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    producto.listaColores.chunked(2).forEach { rowColors ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowColors.forEach { color ->
                                FilterChip(
                                    selected = selectedColor == color,
                                    onClick = { onColorSelected(color) },
                                    label = { Text(color) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowColors.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // Cantidad
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Cantidad",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                var expandedCantidad by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expandedCantidad,
                    onExpandedChange = { expandedCantidad = it }
                ) {
                    OutlinedTextField(
                        value = cantidad.toString(),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCantidad)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCantidad,
                        onDismissRequest = { expandedCantidad = false }
                    ) {
                        (1..minOf(producto.stocks, 10)).forEach { num ->
                            DropdownMenuItem(
                                text = { Text(num.toString()) },
                                onClick = {
                                    onCantidadChanged(num)
                                    expandedCantidad = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Agregar al Carrito
            Button(
                onClick = onAgregarAlCarrito,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = selectedTalla != null && selectedColor != null && producto.stocks > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Agregar al Carrito",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            if (selectedTalla == null || selectedColor == null) {
                Text(
                    text = "Por favor selecciona talla y color",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}