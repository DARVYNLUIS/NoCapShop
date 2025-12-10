package com.edu.darvyn.nocap.presetantion.ordenCompras.OrdenCompraDetails

import android.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.edu.darvyn.nocap.domain.model.CarritoItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrdenCompraScreen(
    viewModel: OrdenCompraViewModel = hiltViewModel(),
    ordenId: Int?,
    usuarioId: Int?,
    onNavigateBack: () -> Unit,
    onNavigateToInicio: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(ordenId, usuarioId) {
        if (ordenId != null && ordenId > 0) {
            viewModel.onEvent(OrdenCompraUiEvent.LoadOrden(ordenId))
        } else if (usuarioId != null && usuarioId > 0) {
            viewModel.onEvent(OrdenCompraUiEvent.Load(usuarioId))
        } else {
            viewModel.onEvent(OrdenCompraUiEvent.DismissError)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth()) {
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

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Orden de Compra",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
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
            }

            // Contenido
            when {
                state.isLoading && state.carrito == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null && state.carrito == null -> {
                    ErrorContent(
                        error = state.error ?: "Error desconocido",
                        onDismiss = { viewModel.onEvent(OrdenCompraUiEvent.DismissError) },
                        onNavigateBack = onNavigateBack
                    )
                }
                state.carrito != null -> {
                    OrdenCompraContent(
                        state = state,
                        onPagarAhora = { viewModel.onEvent(OrdenCompraUiEvent.PagarAhora(state.carrito?.usuarioId))},
                        onPagarDespues = { viewModel.onEvent(OrdenCompraUiEvent.PagarDespues(state.carrito?.usuarioId)) }
                    )
                }
            }
        }

        if (state.showConfirmation && state.ordenCreada != null) {
            ConfirmacionOrdenDialog(
                numeroFactura = state.ordenCreada?.numeroOrden ,
                pagado = state.ordenCreada!!.pagado,
                onDismiss = {
                    viewModel.onEvent(OrdenCompraUiEvent.DismissConfirmation)
                    onNavigateToInicio()
                }
            )
        }

        if (state.error != null && state.carrito != null) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = {
                        viewModel.onEvent(OrdenCompraUiEvent.DismissError)
                    }) {
                        Text("OK")
                    }
                }
            ) {
                Text(state.error ?: "Error")
            }
        }
    }
}


@Composable
private fun OrdenCompraContent(
    state: OrdenCompraUiState,
    onPagarAhora: () -> Unit,
    onPagarDespues: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            OrdenCompraHeader()
        }

        item {
            FacturaInfo(
                numeroFactura = state.ordenCreada?.numeroOrden ?: "Generando",
                fecha = state.ordenCreada?.fechaCompra ?:  SimpleDateFormat("d 'de' MMMM 'de' yyyy ", Locale("es", "DO")).format(Date()),
                estado = if (state.ordenCreada?.pagado == true) "Pagado" else "Pendiente"
            )
        }

        item {
            Text(
                text = "PRODUCTOS",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(state.carrito?.items ?: emptyList()) { item ->
            ProductoCheckoutCard(item = item)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                ResumenCheckout(
                    subtotal = state.subtotal,
                    itbis = state.itbis,
                    envio = state.envio,
                    total = state.total,
                    envioGratis = state.envioGratis,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                        .padding(horizontal = 54.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Gracias por tu compra en NoCap",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        if (state.ordenCreada == null || !state.ordenCreada.pagado ){
            item {
                BotonesAccion(
                    onPagarAhora = onPagarAhora,
                    onPagarDespues = onPagarDespues,
                    pagosPendientes = state.pagosPendientes,
                    maxPagosPendientes = state.maxPagosPendientes,
                    isLoading = state.isLoading
                )
            }

            if (state.pagosPendientes > 0) {
                item {
                    InfoCard(
                        icon = Icons.Default.Info,
                        text = "Tienes ${state.pagosPendientes} pago(s) pendiente(s). " +
                                "Máximo permitido: ${state.maxPagosPendientes}"
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OrdenCompraHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "NoCap",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Tu tienda de gorras premium",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FacturaInfo(
    numeroFactura: String?,
    fecha: String,
    estado: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Factura N°: ",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = numeroFactura ?:"Generando",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Fecha: ",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = fecha,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (estado == "Pagado") Color(0xFF4CAF50) else MaterialTheme.colorScheme.tertiary
            ) {
                Text(
                    text = estado,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
private fun InfoCard(
    icon: ImageVector,
    text: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onDismiss: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            onDismiss()
            onNavigateBack()
        }) {
            Text("Volver")
        }
    }
}

@Composable
private fun ConfirmacionOrdenDialog(
    numeroFactura: String?,
    pagado: Boolean,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "¡Orden Creada!",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Tu orden ha sido creada exitosamente")
                Text(
                    text = "Factura: $numeroFactura",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Estado: ${if (pagado) "Pagado" else "Pendiente de pago"}",
                    color = if (pagado) Color(0xFF4CAF50) else MaterialTheme.colorScheme.tertiary
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Ir al Inicio")
            }
        }
    )
}

// Función para formatear precios
fun formatPrice(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(price)
}



// Componente para mostrar cada producto en el checkout
@Composable
fun ProductoCheckoutCard(
    item: CarritoItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(60.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                item.productoImagen?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = item.productoNombre,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.productoNombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (item.talla.isNotBlank()) {
                        Text(
                            text = "Talla: ${item.talla}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (item.color.isNotBlank()) {
                        Text(
                            text = "● ${item.color}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatPrice(item.subtotal),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${formatPrice(item.precioProducto)} x ${item.cantidad}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Componente para el resumen de costos
@Composable
fun ResumenCheckout(
    subtotal: Double,
    itbis: Double,
    envio: Double,
    total: Double,
    envioGratis: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Subtotal",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = formatPrice(subtotal),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ITBIS (18%)",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = formatPrice(itbis),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Envío",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = if (envioGratis) "Gratis" else formatPrice(envio),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (envioGratis) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurface
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatPrice(total),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Componente para los botones de acción
@Composable
fun BotonesAccion(
    onPagarAhora: () -> Unit,
    onPagarDespues: () -> Unit,
    pagosPendientes: Int,
    maxPagosPendientes: Int,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onPagarAhora,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pagar Ahora",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        OutlinedButton(
            onClick = onPagarDespues,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading && pagosPendientes < maxPagosPendientes,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Pagar Después",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}