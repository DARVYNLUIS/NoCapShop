package com.edu.darvyn.nocap.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object PanelAdministracion : Screen()

    @Serializable
    data object Catalogo : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object CrearUsuario : Screen()

    @Serializable
    data object Categorias : Screen()

    @Serializable
    data object Marcas : Screen()

    @Serializable
    data object Productos : Screen()

    @Serializable
    data object Carrito: Screen()

    @Serializable
    data class OrdenCompras(val carritoId: Int?, val ordenCompraId: Int?): Screen()

    @Serializable
    data object Pedidos: Screen()

    @Serializable
    data class ListOrdenCompras(val usuarioId: Int): Screen()

    @Serializable
    data class PedidosDetalles(val pedidoId: Int): Screen()


    @Serializable
    data class ObserveProductos(val productoId : Int) : Screen()
}

data class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Catalogo, "Catálogo", Icons.Default.Home),
    BottomNavItem(Screen.Carrito, "Carrito", Icons.Default.ShoppingCart),
    BottomNavItem(Screen.Pedidos, "Pedidos", Icons.Default.DeliveryDining),
)