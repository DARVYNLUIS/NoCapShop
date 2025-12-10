package com.edu.darvyn.nocap.presetantion.pedidos


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.EstadoPedido
import com.edu.darvyn.nocap.domain.model.Pedido
import com.edu.darvyn.nocap.presentation.pedido.PedidoViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoDetalleScreen(
    pedidoId: Int,
    viewModel: PedidoViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(pedidoId) {
        viewModel.onEvent(PedidosUiEvent.LoadPedidoById(pedidoId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Pedido") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.pedidoSeleccionado != null -> {
                val pedido = uiState.pedidoSeleccionado!!

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Estado del pedido
                    EstadoCard(pedido.estado)

                    // Información general
                    InfoCard(
                        title = "Información General",
                        icon = Icons.Default.Info
                    ) {
                        InfoRow("Número de pedido", "#${pedido.pedidoId}")
                        InfoRow("Fecha de creación", pedido.fechaCreacion)
                        InfoRow("Entrega estimada", pedido.fechaEntregaEstimada)
                        InfoRow("Orden de compra", "#${pedido.ordenCompraId}")
                    }


                    // Timeline de estados
                    TimelineCard(estadoActual = pedido.estado)

                    // Botones de acción
                    if (pedido.estado == EstadoPedido.PENDIENTE ||
                        pedido.estado == EstadoPedido.EN_PROCESO) {
                        Button(
                            onClick = { showCancelDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(Icons.Default.Cancel, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cancelar Pedido")
                        }
                    }
                }

                // Dialogs
                if (showCancelDialog) {
                    AlertDialog(
                        onDismissRequest = { showCancelDialog = false },
                        title = { Text("Cancelar Pedido") },
                        text = { Text("¿Estás seguro de que deseas cancelar este pedido?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.onEvent(PedidosUiEvent.CancelarPedido(pedidoId))
                                    showCancelDialog = false
                                }
                            ) {
                                Text("Confirmar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showCancelDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }

            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Pedido no encontrado")
                }
            }
        }
    }

    // Success/Error handling
    LaunchedEffect(uiState.showSuccess) {
        if (uiState.showSuccess) {
            kotlinx.coroutines.delay(2000)
            viewModel.onEvent(PedidosUiEvent.ClearSuccess)
        }
    }
}

@Composable
fun EstadoCard(estado: EstadoPedido) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (estado) {
                EstadoPedido.PENDIENTE -> MaterialTheme.colorScheme.secondaryContainer
                EstadoPedido.EN_PROCESO -> MaterialTheme.colorScheme.tertiaryContainer
                EstadoPedido.ENVIADO -> MaterialTheme.colorScheme.primaryContainer
                EstadoPedido.ENTREGADO -> MaterialTheme.colorScheme.secondaryContainer
                EstadoPedido.CANCELADO -> MaterialTheme.colorScheme.errorContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Estado actual",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = estado.estado,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = when (estado) {
                    EstadoPedido.PENDIENTE -> Icons.Default.Schedule
                    EstadoPedido.EN_PROCESO -> Icons.Default.Build
                    EstadoPedido.ENVIADO -> Icons.Default.LocalShipping
                    EstadoPedido.ENTREGADO -> Icons.Default.CheckCircle
                    EstadoPedido.CANCELADO -> Icons.Default.Cancel
                },
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    action: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                action?.invoke()
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TimelineCard(estadoActual: EstadoPedido) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Seguimiento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            val estados = listOf(
                EstadoPedido.PENDIENTE,
                EstadoPedido.EN_PROCESO,
                EstadoPedido.ENVIADO,
                EstadoPedido.ENTREGADO
            )

            estados.forEachIndexed { index, estado ->
                val isActive = estado.ordinal <= estadoActual.ordinal &&
                        estadoActual != EstadoPedido.CANCELADO

                TimelineItem(
                    estado = estado,
                    isActive = isActive,
                    isLast = index == estados.size - 1
                )
            }
        }
    }
}

@Composable
fun TimelineItem(
    estado: EstadoPedido,
    isActive: Boolean,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isActive) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (!isLast) {
                Divider(
                    modifier = Modifier
                        .width(2.dp)
                        .height(32.dp),
                    color = if (isActive)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = estado.estado,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
            color = if (isActive)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
