package com.edu.darvyn.nocap.data.repository

import com.edu.darvyn.nocap.data.local.dao.CarritoDao
import com.edu.darvyn.nocap.data.local.dao.OrdenCompraDao
import com.edu.darvyn.nocap.data.local.dao.PedidoDao
import com.edu.darvyn.nocap.data.local.entities.OrdenCompraEntity
import com.edu.darvyn.nocap.data.local.entities.PedidosEntity
import com.edu.darvyn.nocap.data.local.entities.fechaEntregaAleatoria
import com.edu.darvyn.nocap.data.local.entities.obtenerFechaActual
import com.edu.darvyn.nocap.data.local.mappers.toCarritoItemsCompatible
import com.edu.darvyn.nocap.data.local.mappers.toModel
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.EstadoPedido
import com.edu.darvyn.nocap.domain.model.OrdenCompra
import com.edu.darvyn.nocap.domain.repository.OrdenCompraRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrdenCompraRepositoryImpl @Inject constructor(
    private val ordenCompraDao: OrdenCompraDao,
    private val carritoDao: CarritoDao,
    private val pedidoDao: PedidoDao
) : OrdenCompraRepository {

    override suspend fun crearOrden(
        carritoId: Int,
        usuarioId: Int,
        pagarAhora: Boolean
    ): Resource<OrdenCompra> {
        return try {

            val orden = ordenCompraDao.getByCarritoId(carritoId)
            if (orden != null){
                if (pagarAhora && !orden.pagado) {
                    ordenCompraDao.updatePagado(orden.ordenCompraId!!, true)
                    pedidoDao.savePedido(
                        PedidosEntity(
                            ordenCompraId = orden.ordenCompraId,
                            usuarioId = usuarioId,
                            estado = EstadoPedido.PENDIENTE.estado,
                            fechaCreacion = obtenerFechaActual(),
                            fechaEntregaEstimada = fechaEntregaAleatoria()
                        )
                    )
                }
                val carrito = carritoDao.getCarritoWithDetailsById(orden.carritoId)
                    ?: return Resource.Error("Carrito no encontrado")

                val items = carrito.details.toCarritoItemsCompatible()

                return Resource.Success(orden.toModel().copy(items = items))

            }else {

                val carrito = carritoDao.observeCarritoWithDetails(usuarioId).first()
                    ?: return Resource.Error("Carrito no encontrado")

                if (carrito.details.isEmpty()) {
                    return Resource.Error("El carrito está vacío")
                }

                val items = carrito.details.toCarritoItemsCompatible()

                val subtotal = items.sumOf { it.cantidad * it.precioProducto }
                val itbis = subtotal * 0.18
                val montoTotal = subtotal + itbis

                val nuevaOrden = OrdenCompraEntity(
                    ordenCompraId = null,
                    carritoId = carritoId,
                    usuarioId = usuarioId,
                    pagado = pagarAhora,
                    montoTotal = montoTotal,
                    itbis = itbis.toFloat(),
                    fechaCompra = obtenerFechaActual()
                )
                val ordenId = ordenCompraDao.saveOrdenCompra(nuevaOrden)

                carritoDao.marcarComoPedido(carritoId)

                val ordenCreada = ordenCompraDao.getById(ordenId.toInt())
                    ?: return Resource.Error("Error al crear orden")

                if (ordenCreada.pagado) {
                    pedidoDao.savePedido(
                        PedidosEntity(
                            ordenCompraId = ordenCreada.ordenCompraId!!,
                            usuarioId = ordenCreada.usuarioId,
                            estado = EstadoPedido.PENDIENTE.estado,
                            fechaCreacion = obtenerFechaActual(),
                            fechaEntregaEstimada = fechaEntregaAleatoria()
                        )
                    )
                }

                Resource.Success(ordenCreada.toModel().copy(items = items))
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al crear orden")
        }
    }
    override suspend fun getById(id: Int): OrdenCompra {
        val orden = ordenCompraDao.getById(id)
        return orden!!.toModel()
    }

    override fun observeById(id: Int): Flow<Resource<OrdenCompra>> {
        return ordenCompraDao.observeById(id).map { orden ->
            if (orden != null) {
                Resource.Success(orden.toModel())
            } else {
                Resource.Error("Orden no encontrada")
            }
        }
    }

    override fun observeByUsuario(usuarioId: Int): Flow<Resource<List<OrdenCompra>>> {
        return ordenCompraDao.observeByUsuario(usuarioId).map { ordenes ->
            try {

                val ordenesConCarrito = ordenes.map { entity ->

                    val carrito = carritoDao.getCarritoWithDetailsById(entity.carritoId)
                    val items = carrito?.details?.toCarritoItemsCompatible()

                    entity.toModel().copy(items = items ?: emptyList())
                }

                Resource.Success(ordenesConCarrito)

            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error al cargar órdenes")
            }
        }
    }

    override fun observePendientesByUsuario(usuarioId: Int): Flow<Resource<List<OrdenCompra>>> {
        return ordenCompraDao.observePendientesByUsuario(usuarioId).map { ordenes ->
            try {
                Resource.Success(ordenes.map { it.toModel() })
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error al cargar órdenes pendientes")
            }
        }
    }

    override fun observePagadasByUsuario(usuarioId: Int): Flow<Resource<List<OrdenCompra>>> {
        return ordenCompraDao.observePagadasByUsuario(usuarioId).map { ordenes ->
            try {
                Resource.Success(ordenes.map { it.toModel() })
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error al cargar órdenes pagadas")
            }
        }
    }

    override suspend fun marcarComoPagado(ordenId: Int): Resource<Unit> {
        return try {
            ordenCompraDao.updatePagado(ordenId, true)
            val orden = ordenCompraDao.getById(ordenId)
            pedidoDao.savePedido(PedidosEntity(
                ordenCompraId = orden?.ordenCompraId!!,
                usuarioId = orden.usuarioId,
                estado = EstadoPedido.PENDIENTE.estado,
                fechaCreacion = obtenerFechaActual(),
                fechaEntregaEstimada = fechaEntregaAleatoria()
            ))
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al marcar como pagado")
        }
    }

    override suspend fun countPagosPendientes(usuarioId: Int): Int {
        return try {
            ordenCompraDao.countPagosPendientes(usuarioId)
        } catch (_: Exception) {
            0
        }
    }
}