package com.edu.darvyn.nocap.presetantion.usuario.PerfilUsuario

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.outlined.Blinds
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PerfilUsuarioScreen(
    viewModel: PerfilUsuarioViewModel = hiltViewModel(),
    onBack: () -> Unit,
    goToOrdenesCompras: (Int) -> Unit,
    goToPedidos: (Int) -> Unit,
    goToPanelAdmin: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PerfilUsuario(
        state,
        viewModel::onEvent,
        onBack,
        goToOrdenesCompras,
        goToPedidos,
        goToPanelAdmin
    )
}

@Composable
fun PerfilUsuario(
    state: PerfilUsuarioUiState,
    onEvent: (PerfilUsuarioUiEvent) -> Unit,
    onBack: () -> Unit,
    goToOrdenesCompras: (Int) -> Unit,
    goToPedidos: (Int) -> Unit,
    goToPanelAdmin: (Int) -> Unit

) {
    var showSalirDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        onBack()

                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        "Cuenta",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(onClick = {
                        showSalirDialog = true

                    }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ExitToApp,
                            "Cerrar sesión",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Contenido
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Saludo
                Text(
                    text = "hola, " + state.usuario?.nombres,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column {
                        MenuOption(
                            icon = Icons.Outlined.ShoppingBag,
                            title = "Pedidos",
                            onClick = {
                                goToPedidos(state.usuario?.usuarioId!!)
                            }
                        )

                        MenuOption(
                            icon = Icons.Outlined.Blinds,
                            title = "Ordenes de Compras",
                            onClick = {
                                goToOrdenesCompras(state.usuario?.usuarioId!!)
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        if (state.usuario?.rol != 1) {
                            MenuOption(
                                icon = Icons.Default.PersonPin,
                                title = "Panel de administrador",
                                onClick = {
                                    goToPanelAdmin(state.usuario?.usuarioId!!)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))



            }
        }

        // Snackbar
    }

    if (showSalirDialog) {
        AlertDialog(
            onDismissRequest = { showSalirDialog = false },
            icon = {
                Icon(
                    Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(
                    text = "¿Cerrar Sesion?",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "Esta seguro de cerra la sesion",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEvent(PerfilUsuarioUiEvent.CerrarSesion)
                        showSalirDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSalirDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun MenuOption(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ir",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}