package com.edu.darvyn.nocap.presetantion.productos.edit

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.edu.darvyn.nocap.domain.model.Categoria
import com.edu.darvyn.nocap.domain.model.Marcas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProducto(
    viewModel: EditProductoViewModel = hiltViewModel(),
    onBack: () -> Unit,
    productoId: Int
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(productoId != 0) {
        viewModel.onEvent(EditProductoUiEvent.LoadProducto(productoId))
    }

    EditProductoDialog(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductoDialog(
    state: EditProductoUiState,
    onEvent: (EditProductoUiEvent) -> Unit,
    onBack: () -> Unit
) {
    var expandedCategoria by remember { mutableStateOf(false) }
    var expandedMarca by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isProcessingImage by remember { mutableStateOf(false) }

    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isProcessingImage = true
            scope.launch {
                val savedPath = saveImageToInternalStorage(context, it)
                savedPath?.let { path ->
                    onEvent(EditProductoUiEvent.ImagenChange(path))
                }
                isProcessingImage = false
            }
        }
    }

    Dialog(onDismissRequest = onBack) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (state.productoId != null) "Editar Producto" else "Crear Nuevo Producto",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Agrega un nuevo producto al catálogo",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección de Imagen del Producto
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Imagen del producto",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Contenedor de la imagen con click para seleccionar
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(
                                    width = 2.dp,
                                    color = if (state.productoImagen.isNullOrEmpty())
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    else
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = !isProcessingImage) {
                                    imagePickerLauncher.launch("image/*")
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                isProcessingImage -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Procesando...",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                state.productoImagen.isNullOrEmpty() -> {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AddPhotoAlternate,
                                            contentDescription = "Seleccionar imagen",
                                            modifier = Modifier.size(48.dp),
                                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Toca para agregar",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                else -> {
                                    SubcomposeAsyncImage(
                                        model = if (state.productoImagen.startsWith("/")) {
                                            File(state.productoImagen)
                                        } else {
                                            state.productoImagen
                                        },
                                        contentDescription = "Imagen del producto",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop,
                                        loading = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(32.dp)
                                                )
                                            }
                                        },
                                        error = {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Image,
                                                        contentDescription = "Error al cargar",
                                                        modifier = Modifier.size(36.dp),
                                                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        "Error al cargar",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.error
                                                    )
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón para cambiar imagen
                        if (!state.productoImagen.isNullOrEmpty() && !isProcessingImage) {
                            OutlinedButton(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text("Cambiar imagen", style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        if (state.productoImagenError != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = state.productoImagenError,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.align(Alignment.Start)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre del producto y Marca
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Nombre del producto", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = state.nombre ?: "",
                            onValueChange = { onEvent(EditProductoUiEvent.NombreChange(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Ej: Gorra Nike Sw", style = MaterialTheme.typography.bodySmall) },
                            textStyle = MaterialTheme.typography.bodySmall,
                            singleLine = true
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Marca", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        ExposedDropdownMenuBox(
                            expanded = expandedMarca,
                            onExpandedChange = { expandedMarca = it }
                        ) {
                            OutlinedTextField(
                                value = state.listaMarcas.find { it.marcaId == state.marcaId }?.nombre ?: "Elige",
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                textStyle = MaterialTheme.typography.bodySmall,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMarca) }
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMarca,
                                onDismissRequest = { expandedMarca = false }
                            ) {
                                state.listaMarcas.forEach { marca ->
                                    DropdownMenuItem(
                                        text = { Text(marca.nombre, style = MaterialTheme.typography.bodySmall) },
                                        onClick = {
                                            onEvent(EditProductoUiEvent.MarcaChange(marca.marcaId ?: 0))
                                            expandedMarca = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Categoría", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        ExposedDropdownMenuBox(
                            expanded = expandedCategoria,
                            onExpandedChange = { expandedCategoria = it }
                        ) {
                            OutlinedTextField(
                                value = state.listaCategoria.find { it.categoriaId == state.categoriaId }?.nombre ?: "Elige",
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                textStyle = MaterialTheme.typography.bodySmall,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria) }
                            )
                            ExposedDropdownMenu(
                                expanded = expandedCategoria,
                                onDismissRequest = { expandedCategoria = false }
                            ) {
                                state.listaCategoria.forEach { categoria ->
                                    DropdownMenuItem(
                                        text = { Text(categoria.nombre, style = MaterialTheme.typography.bodySmall) },
                                        onClick = {
                                            onEvent(EditProductoUiEvent.CategoriaChange(categoria.categoriaId ?: 0))
                                            expandedCategoria = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(0.7f)) {
                        Text("Precio ($)", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = state.precioVenta ?: "",
                            onValueChange = { onEvent(EditProductoUiEvent.PrecioVentaChange(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodySmall,
                            placeholder = { Text("29.9", style = MaterialTheme.typography.bodySmall) },
                            singleLine = true
                        )
                    }

                    Column(modifier = Modifier.weight(0.7f)) {
                        Text("Stock", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = state.stocks ?: "",
                            onValueChange = { onEvent(EditProductoUiEvent.StockChange(it)) },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodySmall,
                            placeholder = { Text("100", style = MaterialTheme.typography.bodySmall) },
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Descripción
                Text("Descripción", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = state.descripcion ?: "",
                    onValueChange = { onEvent(EditProductoUiEvent.DescripcionChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodySmall,
                    placeholder = { Text("Descripción del producto...", style = MaterialTheme.typography.bodySmall) },
                    minLines = 2,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Tamaños disponibles", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("S", "M", "L", "XL").forEach { tamano ->
                        FilterChip(
                            selected = state.listaTamano.contains(tamano),
                            onClick = { onEvent(EditProductoUiEvent.ToggleTamano(tamano)) },
                            label = { Text(tamano, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Colores disponibles", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Negro", "Blanco", "Azul", "Rojo").forEach { color ->
                        FilterChip(
                            selected = state.listaColores.contains(color),
                            onClick = { onEvent(EditProductoUiEvent.ToggleColor(color)) },
                            label = { Text(color, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Verde", "Gris", "Rosa", "Amarillo").forEach { color ->
                        FilterChip(
                            selected = state.listaColores.contains(color),
                            onClick = { onEvent(EditProductoUiEvent.ToggleColor(color)) },
                            label = { Text(color, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onEvent(EditProductoUiEvent.Save)
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !isProcessingImage
                ) {
                    Text(
                        if (state.productoId != null) "Guardar Cambios" else "Crear Producto",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}

// Función para guardar la imagen en almacenamiento interno
private suspend fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "producto_${UUID.randomUUID()}.jpg"
            val directory = File(context.filesDir, "productos_images")

            if (!directory.exists()) {
                directory.mkdirs()
            }

            val file = File(directory, fileName)
            val outputStream = FileOutputStream(file)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun EditProductoDialogPreview() {
    val mockState = EditProductoUiState(
        isNew = true,
        productoId = null,
        nombre = "Gorra Nike",
        descripcion = "Gorra deportiva de alta calidad",
        precioVenta = "29.99",
        stocks = "50",
        productoImagen = null,
        categoriaId = 1,
        marcaId = 1,
        listaTamano = listOf("S", "M"),
        listaColores = listOf("Negro", "Blanco"),
        listaCategoria = listOf(
            Categoria(
                categoriaId = 1, nombre = "Gorras",
                descripcion = TODO(),
                activa = TODO()
            ),
            Categoria(
                categoriaId = 2, nombre = "Camisetas",
                descripcion = TODO(),
                activa = TODO()
            )
        ),
        listaMarcas = listOf(
            Marcas(
                marcaId = 1, nombre = "Nike",
                activa = TODO()
            ),
            Marcas(
                marcaId = 2, nombre = "Adidas",
                activa = TODO()
            )
        )
    )

    EditProductoDialog(
        state = mockState,
        onEvent = {},
        onBack = {}
    )
}