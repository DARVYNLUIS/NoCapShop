package com.edu.darvyn.nocap.presetantion.catalago.listProductos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.edu.darvyn.nocap.R
import com.edu.darvyn.nocap.domain.model.Marcas
import com.edu.darvyn.nocap.domain.model.Producto
import com.edu.darvyn.nocap.presetantion.catalago.listProductos.CatalogoList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoList(
    viewModel: CatalogoViewModel = hiltViewModel(),
    goToUsuario: () -> Unit,
    goToDetalle: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = goToUsuario) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Usuario",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.logo_no_cap),
                    contentDescription = "Logo",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Unspecified
                )

                IconButton(onClick = { viewModel.onEvent(ListProductosUiEvent.ModalOn(true))  }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "FilterList",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        if (state.mostrarModal) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.onEvent(ListProductosUiEvent.ModalOn(false))  },
                sheetState = sheetState
            ) {
                FiltrosMarcasContent(
                    viewModel::onEvent,
                    marcas = state.marcas,
                    selectedMarcas = state.selectedMarcaIds,
                    onDismiss = { viewModel.onEvent(ListProductosUiEvent.ModalOn(false))}
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Catálogo",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${state.productos.size} productos encontrados",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }

        when {
            state.isLoading && state.productos.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            state.message != null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message ?: "Error",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
            state.productos.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay productos disponibles",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items (state.productos) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { goToDetalle(producto.productoId!!) },
                        marcas = state.marcas
                    )
                }
            }
        }
    }
}

@Composable
fun FiltrosMarcasContent(
    onEvent: (ListProductosUiEvent) -> Unit,
    marcas: List<Marcas>,
    selectedMarcas: List<Int>?,
    onDismiss: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Filtrar por Marca",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(marcas) { marca ->
                MarcaFilterItem(
                    marca = marca,
                    isSelected = selectedMarcas!!.contains(marca.marcaId),
                    onCheckedChange = {
                        onEvent(ListProductosUiEvent.SeleccionarMarcar(marca.marcaId!!))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    onEvent(ListProductosUiEvent.LimpiarFiltro)

                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Limpiar")
            }

            Button(
                onClick = {
                    onEvent(ListProductosUiEvent.AplicarFiltro)
                    onDismiss()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Aplicar")
            }
        }
    }
}

@Composable
fun MarcaFilterItem(
    marca: Marcas,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isSelected) }
            .background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = marca.nombre,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Checkbox(
            checked = isSelected,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }


}

@Composable
fun ProductoCard(
    producto: Producto,
    marcas: List<Marcas>,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(producto.productoId!!) }),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AsyncImage(
                    model = producto.productoImagen,
                    contentDescription = producto.nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = marcas.find { it.marcaId == producto.marcaId }?.nombre ?: "Desconocido",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$${String.format("%.2f", producto.precioVenta)} Dolares",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onClick(producto.productoId!!) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Ver Detalles",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatalogoListPreview() {
    val marcasEjemplo = listOf(
        Marcas(marcaId = 1, nombre = "Nike", activa = true),
        Marcas(marcaId = 2, nombre = "Jordan", activa = true),
        Marcas(marcaId = 3, nombre = "Adidas", activa = true)
    )

    val productosEjemplo = listOf(
        Producto(
            productoId = 1,
            nombre = "Gorra Jordan",
            descripcion = "Gorra deportiva Jordan",
            fechaCreacion = "2025-12-10",
            productoImagen = "https://picsum.photos/300",
            precioVenta = 25.0,
            stocks = 10,
            categoriaId = 1,
            marcaId = 2,
            activo = true,
            listaTamanos = listOf("S","M","L"),
            listaColores = listOf("Rojo","Negro")
        ),
        Producto(
            productoId = 2,
            nombre = "Gorra Nike",
            descripcion = "Gorra deportiva Nike",
            fechaCreacion = "2025-12-09",
            productoImagen = "https://picsum.photos/301",
            precioVenta = 30.0,
            stocks = 15,
            categoriaId = 1,
            marcaId = 1,
            activo = true,
            listaTamanos = listOf("M","L","XL"),
            listaColores = listOf("Blanco","Negro")
        )
    )

    var state by remember {
        mutableStateOf(
            CatalogoUiState(
                isLoading = false,
                productos = productosEjemplo,
                marcas = marcasEjemplo,
                selectedMarcaIds = emptyList(),
                mostrarModal = false,
                message = null
            )
        )
    }

    val fakeViewModel = object {
        val stateFlow: StateFlow<CatalogoUiState> = MutableStateFlow(state)
        fun onEvent(event: ListProductosUiEvent) {
            state = when (event) {
                is ListProductosUiEvent.ModalOn -> state.copy(mostrarModal = event.state)
                is ListProductosUiEvent.SeleccionarMarcar -> {
                    val selected = state.selectedMarcaIds!!.toMutableList()
                    if (selected.contains(event.marcaId)) selected.remove(event.marcaId) else selected.add(event.marcaId)
                    state.copy(selectedMarcaIds = selected)
                }
                is ListProductosUiEvent.LimpiarFiltro -> state.copy(selectedMarcaIds = emptyList())
                is ListProductosUiEvent.AplicarFiltro -> state
                else -> state
            }
        }
    }

    CatalogoListPreviewContent(
        state = state,
        onEvent = fakeViewModel::onEvent,
        goToUsuario = {},
        goToDetalle = {}
    )
}

@Composable
fun CatalogoListPreviewContent(
    state: CatalogoUiState,
    onEvent: (ListProductosUiEvent) -> Unit,
    goToUsuario: () -> Unit,
    goToDetalle: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(
            text = "Catálogo (${state.productos.size} productos)",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.productos) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { goToDetalle(producto.productoId!!) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(producto.nombre, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = "$${String.format("%.2f", producto.precioVenta)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
