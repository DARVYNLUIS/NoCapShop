package com.edu.darvyn.nocap.data.repository

import com.edu.darvyn.nocap.data.local.dao.CarritoDao
import com.edu.darvyn.nocap.data.local.dao.ProductoDao
import com.edu.darvyn.nocap.data.local.entities.CarritoDetailsEntity
import com.edu.darvyn.nocap.data.local.entities.CarritoEntity
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Carrito
import com.edu.darvyn.nocap.domain.model.CarritoItem
import com.edu.darvyn.nocap.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.map
import kotlin.collections.sumOf

class CarritoRepositoryImpl @Inject constructor(
    private val carritoDao: CarritoDao,
    private val productoDao: ProductoDao
) : CarritoRepository {

    override fun observeCarrito(usuarioId: Int): Flow<Resource<Carrito>> {
        return carritoDao.observeCarritoWithDetails(usuarioId).map { carritoWithDetails ->
            try {
                if (carritoWithDetails == null) {
                    Resource.Success(
                        Carrito(
                            carritoId = null,
                            usuarioId = usuarioId,
                            montoTotal = 0.0,
                            fechaCreacion = "",
                            items = emptyList()
                        )
                    )
                } else {
                    val items = carritoWithDetails.details.map { detail ->
                        val producto = productoDao.observeById(detail.productoId)
                        CarritoItem(
                            carritoDetailsId = detail.carritoDetailsId,
                            productoId = detail.productoId,
                            productoNombre = producto.nombre,
                            productoImagen = producto.productoImagen,
                            cantidad = detail.cantidad,
                            precioProducto = detail.precioProducto,
                            color = detail.color,
                            talla = detail.talla
                        )
                    }

                    Resource.Success(
                        Carrito(
                            carritoId = carritoWithDetails.carrito.carritoId,
                            usuarioId = carritoWithDetails.carrito.usuarioId,
                            montoTotal = items.sumOf { it.subtotal },
                            fechaCreacion = carritoWithDetails.carrito.fechaCreacion,
                            items = items
                        )
                    )
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error al cargar carrito")
            }
        }
    }

    override suspend fun getById(carritoId: Int): Carrito {
        val carritoWithDetails = carritoDao.getById(carritoId)
            ?: throw Exception("Carrito no encontrado")

        val items = carritoWithDetails.details.map { detail ->
            val producto = productoDao.observeById(detail.productoId)

            CarritoItem(
                carritoDetailsId = detail.carritoDetailsId,
                productoId = detail.productoId,
                productoNombre = producto.nombre,
                productoImagen = producto.productoImagen,
                cantidad = detail.cantidad,
                precioProducto = detail.precioProducto,
                color = detail.color,
                talla = detail.talla
            )
        }

        return Carrito(
            carritoId = carritoWithDetails.carrito.carritoId!!,
            usuarioId = carritoWithDetails.carrito.usuarioId,
            montoTotal = items.sumOf { it.subtotal },
            fechaCreacion = carritoWithDetails.carrito.fechaCreacion,
            items = items
        )
    }


    override suspend fun agregarProducto(
        usuarioId: Int,
        productoId: Int,
        cantidad: Int,
        precio: Double,
        color: String,
        talla: String
    ): Resource<Unit> {
        return try {
            var carrito = carritoDao.getCarritoActivo(usuarioId)
            if (carrito == null || carrito.pedido) {
                // Crear nuevo carrito
                val fechaActual = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(Date())
                carrito = CarritoEntity(
                    carritoId =  null,
                    usuarioId = usuarioId,
                    montoTotal = 0.0,
                    fechaCreacion = fechaActual,
                    pedido = false
                )
               val carritoId = carritoDao.insertCarrito(carrito)
               carrito = carrito.copy(carritoId = carritoId.toInt())
            }

            // Agregar detalle
            val detail = CarritoDetailsEntity(
                carritoDetailsId = null,
                carritoId = carrito.carritoId,
                productoId = productoId,
                cantidad = cantidad,
                precioProducto = precio,
                color = color,
                talla = talla
            )

            carritoDao.insertCarritoDetail(detail)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al agregar producto")
        }
    }

    override suspend fun eliminarItem(carritoDetailsId: Int): Resource<Unit> {
        return try {
            carritoDao.deleteCarritoDetail(carritoDetailsId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al eliminar item")
        }
    }

    override suspend fun actualizarCantidad(carritoDetailsId: Int, cantidad: Int): Resource<Unit> {
        return try {
            carritoDao.updateCantidad(carritoDetailsId, cantidad)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al actualizar cantidad")
        }
    }

    override suspend fun limpiarCarrito(carritoId: Int): Resource<Unit> {
        return try {
            carritoDao.deleteCarrito(carritoId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al limpiar carrito")
        }
    }

    override suspend fun marcarComoPedido(carritoId: Int): Resource<Unit> {
        return try {
            carritoDao.marcarComoPedido(carritoId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al marcar pedido")
        }
    }
}