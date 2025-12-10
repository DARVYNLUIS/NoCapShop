package com.edu.darvyn.nocap.presetantion.pedidos


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edu.darvyn.nocap.domain.model.EstadoPedido
import com.edu.darvyn.nocap.domain.model.Pedido
import com.edu.darvyn.nocap.presentation.pedido.PedidoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoScreen(
    viewModel: PedidoViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigateToDetalle: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf<EstadoPedido?>(null) }



    LaunchedEffect(selectedFilter) {
        viewModel.onEvent(PedidosUiEvent.FiltrarPorEstado(selectedFilter))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterChips(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    ErrorMessage(
                        message = uiState.error!!,
                        onDismiss = { viewModel.onEvent(PedidosUiEvent.ClearError) }
                    )
                }
                uiState.pedidos.isEmpty() -> {
                    EmptyState()
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.pedidos) { pedido ->
                            PedidoCard(
                                pedido = pedido,
                                onClick = { onNavigateToDetalle(pedido.pedidoId) }
                            )
                        }
                    }
                }
            }

            // Success Snackbar
            if (uiState.showSuccess) {
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(3000)
                    viewModel.onEvent(PedidosUiEvent.ClearSuccess)
                }
            }
        }
    }
}

@Composable
fun FilterChips(
    selectedFilter: EstadoPedido?,
    onFilterSelected: (EstadoPedido?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedFilter == null,
                onClick = { onFilterSelected(null) },
                label = { Text("Todos") }
            )
        }
        items(EstadoPedido.entries.toTypedArray()) { estado ->
            FilterChip(
                selected = selectedFilter == estado,
                onClick = { onFilterSelected(estado) },
                label = { Text(estado.estado) }
            )
        }
    }
}

@Composable
fun PedidoCard(
    pedido: Pedido,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${pedido.pedidoId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                EstadoChip(estado = pedido.estado)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Fecha de creación
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Creado: ${pedido.fechaCreacion}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Fecha estimada de entrega
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Entrega estimada: ${pedido.fechaEntregaEstimada}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


        }
    }
}

@Composable
fun EstadoChip(estado: EstadoPedido) {
    val (backgroundColor, textColor) = when (estado) {
        EstadoPedido.PENDIENTE -> Color(0xFFFFF9C4) to Color(0xFFF57F17)
        EstadoPedido.EN_PROCESO -> Color(0xFFBBDEFB) to Color(0xFF1565C0)
        EstadoPedido.ENVIADO -> Color(0xFFC5CAE9) to Color(0xFF283593)
        EstadoPedido.ENTREGADO -> Color(0xFFC8E6C9) to Color(0xFF2E7D32)
        EstadoPedido.CANCELADO -> Color(0xFFFFCDD2) to Color(0xFFC62828)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = estado.estado,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Inventory,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No tienes pedidos",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Tus pedidos aparecerán aquí",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        action = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    ) {
        Text(message)
    }
}