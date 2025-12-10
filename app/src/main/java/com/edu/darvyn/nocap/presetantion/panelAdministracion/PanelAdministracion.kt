package com.edu.darvyn.nocap.presetantion.panelAdministracion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edu.darvyn.nocap.presetantion.panelAdministracion.panelCategorias.ContenidoCategorias
import com.edu.darvyn.nocap.presetantion.panelAdministracion.panelMarcas.MarcasContent
import com.edu.darvyn.nocap.presetantion.panelAdministracion.panelPedidos.PedidosContent
import com.edu.darvyn.nocap.presetantion.panelAdministracion.panelProductos.ProductosContent
import kotlinx.coroutines.delay


@Composable
fun PanelAdministracion(
    volerMenu: () -> Unit,
    goToCategorias: () -> Unit,
    goToMarcas: () -> Unit,
    goToProductos: () -> Unit,
    ) {
    var selectedTab by remember { mutableIntStateOf(1) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val tabs = listOf("Pedidos", "Productos", "Marcas", "Categorías")

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
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            snackbarMessage = "Volver al inicio"
                            showSnackbar = true
                            volerMenu()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                "Volver",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Panel de Administración",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Gestiona tu tienda ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Tabs
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier.fillMaxWidth(),
                        edgePadding = 16.dp
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        title,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            )
                        }
                    }
                }
            }

            when (selectedTab) {
               0 -> PedidosContent(
                )
                1 -> ProductosContent(
                   goToProducto =  goToProductos
                )
                2 -> MarcasContent(
                    goToGestion = goToMarcas
                )
                 3  -> ContenidoCategorias(
                    goToGestion = goToCategorias
                )
            }
        }

        // Snackbar
        if (showSnackbar) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { showSnackbar = false }) {
                        Text("OK")
                    }
                }
            ) {
                Text(snackbarMessage)
            }

            LaunchedEffect(showSnackbar) {
                delay(2000)
                showSnackbar = false
            }
        }
    }
}






