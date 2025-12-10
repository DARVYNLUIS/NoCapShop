package com.edu.darvyn.nocap.presetantion.panelAdministracion.panelPedidos


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edu.darvyn.nocap.domain.model.EstadoPedido
import com.edu.darvyn.nocap.domain.model.Pedido

@Composable
fun PedidosContent(
    viewModel: PanelPedidosViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PedidoScreen(
        pedidos = state.pedidos,
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun PedidoScreen(
    pedidos: List<Pedido?>,
) {
    Column(
        modifier = Modifier.fillMaxSize()
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
                    "Gestión de Pedidos",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "${pedidos.size} pedidos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            // Header de tabla
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "ID Pedido",
                        modifier = Modifier.weight(0.8f),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Estado",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Fecha Creación",
                        modifier = Modifier.weight(1.2f),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Entrega Est.",
                        modifier = Modifier.weight(1.2f),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                HorizontalDivider()
            }

            items(pedidos) { pedido ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "#${pedido?.pedidoId}",
                        modifier = Modifier.weight(0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    EstadoBadge(
                        estado = pedido!!.estado ,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        pedido.fechaCreacion,
                        modifier = Modifier.weight(1.2f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        pedido.fechaEntregaEstimada,
                        modifier = Modifier.weight(1.2f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun EstadoBadge(
    estado: EstadoPedido,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (estado) {
        EstadoPedido.PENDIENTE -> Color(0xFFFFF3CD)
        EstadoPedido.EN_PROCESO -> Color(0xFFCFE2FF)
        EstadoPedido.ENVIADO -> Color(0xFFD1E7DD)
        EstadoPedido.ENTREGADO -> Color(0xFFD1F7E5)
        EstadoPedido.CANCELADO -> Color(0xFFF8D7DA)
    }

    val textColor = when (estado) {
        EstadoPedido.PENDIENTE -> Color(0xFF664D03)
        EstadoPedido.EN_PROCESO -> Color(0xFF084298)
        EstadoPedido.ENVIADO -> Color(0xFF0F5132)
        EstadoPedido.ENTREGADO -> Color(0xFF0A5C36)
        EstadoPedido.CANCELADO -> Color(0xFF842029)
    }

    Text(
        text = estado.estado,
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(backgroundColor)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        style = MaterialTheme.typography.bodySmall,
        color = textColor
    )
}