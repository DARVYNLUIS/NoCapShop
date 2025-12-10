package com.edu.darvyn.nocap.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.edu.darvyn.nocap.presetantion.carrito.CarritoScreen
import com.edu.darvyn.nocap.presetantion.pedidos.PedidoDetalleScreen
import com.edu.darvyn.nocap.presetantion.pedidos.PedidoScreen
import com.edu.darvyn.nocap.presetantion.marcas.list.ListMarca
import com.edu.darvyn.nocap.presetantion.catalago.listProductos.CatalogoList
import com.edu.darvyn.nocap.presetantion.catalago.observeProducto.ObserveProductoScreen
import com.edu.darvyn.nocap.presetantion.categoria.list.ListCategoria
import com.edu.darvyn.nocap.presetantion.ordenCompras.OrdenCompraDetails.OrdenCompraScreen
import com.edu.darvyn.nocap.presetantion.ordenCompras.listOrdenCompras.OrdenesListScreen
import com.edu.darvyn.nocap.presetantion.panelAdministracion.PanelAdministracion
import com.edu.darvyn.nocap.presetantion.productos.list.ListProducto
import com.edu.darvyn.nocap.presetantion.usuario.Login.LoginDecisionScreen
import com.edu.darvyn.nocap.presetantion.usuario.crear.CrearUsuario

@Composable
fun NoCapNavigation(
    navHostController: NavHostController
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.screen::class.qualifiedName,
                        onClick = {
                            navHostController.navigate(item.screen) {
                                popUpTo(navHostController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navHostController,
            startDestination = Screen.Catalogo,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable<Screen.Catalogo> {
                CatalogoList(
                    goToUsuario = {
                        navHostController.navigate(Screen.Login)
                    },

                    goToDetalle = { productoId ->
                        navHostController.navigate(
                            Screen.ObserveProductos(productoId)
                        )
                    }
                )
            }

            composable<Screen.ListOrdenCompras> { entry ->
                val args = entry.toRoute<Screen.ListOrdenCompras>()
                OrdenesListScreen(
                    usuarioId = args.usuarioId,
                    onNavigateBack = { navHostController.navigateUp() },
                    goToDetalle = { ordenId ->
                        navHostController.navigate(Screen.OrdenCompras(carritoId = null, ordenCompraId = ordenId) )
                    }
                )
            }

            composable<Screen.OrdenCompras> { entry ->
                val args = entry.toRoute<Screen.OrdenCompras>()
                OrdenCompraScreen(
                    usuarioId = args.carritoId,
                    ordenId = args.ordenCompraId,
                    onNavigateBack = { navHostController.navigateUp() },
                    onNavigateToInicio = { navHostController.navigate(Screen.Catalogo) }
                )
            }

            composable<Screen.ObserveProductos> { entry ->
                val args = entry.toRoute<Screen.ObserveProductos>()
                ObserveProductoScreen(
                    productoId = args.productoId,
                    onNavigateBack = { navHostController.navigateUp() }
                )
            }

            composable<Screen.Marcas> {
                ListMarca(
                    goToBack = { navHostController.navigateUp() }
                )
            }

            composable<Screen.CrearUsuario> {
                CrearUsuario(
                    goToBack = { navHostController.navigate(Screen.Catalogo) }
                )
            }

            composable<Screen.Login> {
                LoginDecisionScreen(
                    goToBack = { navHostController.navigateUp() },
                    goToCreate = { navHostController.navigate(Screen.CrearUsuario) },
                    goToOrdenesCompras ={
                        navHostController.navigate(Screen.ListOrdenCompras(it))
                    },
                    goToPedidos = {
                        navHostController.navigate(Screen.Pedidos)
                    },
                    goToPanelAdmin = {
                        navHostController.navigate(Screen.PanelAdministracion)
                    }
                )
            }

            composable<Screen.Categorias> {
                ListCategoria(
                    onBack = { navHostController.navigateUp() }
                )
            }

            composable<Screen.Productos> {
                ListProducto(
                    onBack = { navHostController.navigateUp() }
                )
            }

            composable<Screen.Carrito> {
                CarritoScreen(
                    onNavigateBack = { navHostController.navigateUp() },
                    goToPay = { carritoId ->
                        navHostController.navigate(Screen.OrdenCompras(carritoId = carritoId, ordenCompraId = null))
                    }
                )
            }

            composable<Screen.Pedidos> {
                PedidoScreen(
                    onBack = { navHostController.navigateUp() },
                    onNavigateToDetalle = { pedidoId ->
                        navHostController.navigate(Screen.PedidosDetalles(pedidoId))
                    }
                )
            }

            composable<Screen.PedidosDetalles> { entry ->
                val args = entry.toRoute<Screen.PedidosDetalles>()
                PedidoDetalleScreen(
                    pedidoId = args.pedidoId,
                    onBack = { navHostController.navigateUp() },
                )
            }



            composable<Screen.PanelAdministracion> {
                PanelAdministracion(
                    volerMenu = {
                        navHostController.navigateUp()
                    },
                    goToCategorias = {
                        navHostController.navigate(Screen.Categorias)
                    },
                    goToMarcas = {
                        navHostController.navigate(Screen.Marcas)
                    },
                    goToProductos = {
                        navHostController.navigate(Screen.Productos)
                    }
                )
            }
        }
    }
}
