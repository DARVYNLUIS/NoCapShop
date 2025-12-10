package com.edu.darvyn.nocap.presetantion.categoria.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EditCategoria(
    viewModel: EditCategoriaViewModel = hiltViewModel(),
    onBack: () -> Unit,
    categoriaId: Int
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(categoriaId != 0) {
        viewModel.onEvent(EditCategoriaUiEvent.LoadCategoria(categoriaId))
    }

    EditCategoriaDialog(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )

}


@Composable
fun EditCategoriaDialog(
    state: EditCategoriaUiState,
    onEvent: (EditCategoriaUiEvent) -> Unit,
    onBack: () -> Unit
){

    Dialog(onDismissRequest = onBack) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (state.categoriaId != null) "Editar Categoría" else "Crear Nueva Categoría",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (state.categoriaId != null) "Modifica la información" else "Agrega una nueva categoría al catálogo",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.Close,
                            "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Campo Nombre
                Text(
                    text = "Nombre de la categoría",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.categoriaNombre ?: "",
                    onValueChange = { onEvent(EditCategoriaUiEvent.CategoriaNombreChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Ej: Deportes, Electrónica, Moda...")
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Descripción
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.categoriaDescripcion ?: "",
                    onValueChange = { onEvent(EditCategoriaUiEvent.CategoriaDescripcionChange(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Descripción de la categoría")
                    },
                    minLines = 3,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón principal
                Button(
                    onClick = {
                        onEvent(EditCategoriaUiEvent.Save)
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (state.categoriaId != null) "Guardar Cambios" else "Crear Categoría",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón cancelar
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditCategoriaPreview() {
    var state by remember {
        mutableStateOf(
            EditCategoriaUiState(
                categoriaId = 1,
                categoriaNombre = "Deportes",
                categoriaDescripcion = "Categoría para artículos deportivos",
                isNew = false
            )
        )
    }

    val fakeOnEvent: (EditCategoriaUiEvent) -> Unit = { event ->
        state = when (event) {
            is EditCategoriaUiEvent.CategoriaNombreChange -> state.copy(categoriaNombre = event.nombre)
            is EditCategoriaUiEvent.CategoriaDescripcionChange -> state.copy(categoriaDescripcion = event.descripcion)
            else -> state
        }
    }

    EditCategoriaDialog(
        state = state,
        onEvent = fakeOnEvent,
        onBack = {}
    )
}
