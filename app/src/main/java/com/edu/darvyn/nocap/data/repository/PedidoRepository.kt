package com.edu.darvyn.nocap.data.repository


import com.edu.darvyn.nocap.data.local.dao.OrdenCompraDao
import com.edu.darvyn.nocap.data.local.dao.PedidoDao
import com.edu.darvyn.nocap.data.local.entities.PedidosEntity
import com.edu.darvyn.nocap.data.local.entities.fechaEntregaAleatoria
import com.edu.darvyn.nocap.data.local.entities.obtenerFechaActual
import com.edu.darvyn.nocap.data.local.mappers.toModel
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.EstadoPedido
import com.edu.darvyn.nocap.domain.model.Pedido
import com.edu.darvyn.nocap.domain.repository.PedidoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PedidoRepositoryImpl @Inject constructor(
    private val pedidoDao: PedidoDao,
    private val ordenCompraDao: OrdenCompraDao
) : PedidoRepository {

    override suspend fun crearPedido(
        ordenCompraId: Int,
        usuarioId: Int,
    ): Resource<Pedido> {
        return try {
            val orden = ordenCompraDao.getById(ordenCompraId)
                ?: return Resource.Error("Orden de compra no encontrada")

            val pedidoExistente = pedidoDao.getByOrdenCompra(ordenCompraId)
            if (pedidoExistente != null) {
                return Resource.Error("Ya existe un pedido para esta orden")
            }

            val nuevoPedido = PedidosEntity(
                pedidoId = 0,
                ordenCompraId = ordenCompraId,
                usuarioId = usuarioId,
                estado = EstadoPedido.PENDIENTE.estado,
                fechaCreacion = obtenerFechaActual(),
                fechaEntregaEstimada = fechaEntregaAleatoria()
            )

            val pedidoId = pedidoDao.savePedido(nuevoPedido)

            val pedidoCreado = pedidoDao.getById(pedidoId.toInt())
                ?: return Resource.Error("Error al crear pedido")

            Resource.Success(pedidoCreado.toModel())

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al crear pedido")
        }
    }

    override suspend fun getById(id: Int): Pedido? {
        return try {
            pedidoDao.getById(id)?.toModel()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllPedidos(): List<Pedido> {
        return pedidoDao.getAllPedidos().map { it.toModel() }
    }

    override fun observeById(id: Int): Flow<Resource<Pedido>> {
        return pedidoDao.observeById(id).map { pedido ->
            if (pedido != null) {
                Resource.Success(pedido.toModel())
            } else {
                Resource.Error("Pedido no encontrado")
            }
        }
    }

    override fun observeByUsuario(usuarioId: Int): Flow<Resource<List<Pedido>>> {
        return pedidoDao.observeByUsuario(usuarioId).map { pedidos ->
            try {
                Resource.Success(pedidos.map { it.toModel() })
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error al cargar pedidos")
            }
        }
    }

    override suspend fun getByOrdenCompra(ordenCompraId: Int): Pedido? {
        return try {
            pedidoDao.getByOrdenCompra(ordenCompraId)?.toModel()
        } catch (e: Exception) {
            null
        }
    }

    override fun observeByOrdenCompra(ordenCompraId: Int): Flow<Resource<Pedido>> {
        return pedidoDao.observeByOrdenCompra(ordenCompraId).map { pedido ->
            if (pedido != null) {
                Resource.Success(pedido.toModel())
            } else {
                Resource.Error("Pedido no encontrado")
            }
        }
    }

    override suspend fun actualizarEstado(pedidoId: Int, estado: EstadoPedido): Resource<Unit> {
        return try {
            pedidoDao.updateEstado(pedidoId, estado.estado)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al actualizar estado")
        }
    }

    override fun observeByEstado(usuarioId: Int, estado: EstadoPedido): Flow<Resource<List<Pedido>>> {
        return pedidoDao.observeByUsuarioAndEstado(usuarioId, estado.estado).map { pedidos ->
            try {
                Resource.Success(pedidos.map { it.toModel() })
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Error al cargar pedidos")
            }
        }
    }

    override suspend fun countPedidosActivos(usuarioId: Int): Int {
        return try {
            pedidoDao.countPedidosActivos(usuarioId)
        } catch (e: Exception) {
            0
        }
    }

    override suspend fun cancelarPedido(pedidoId: Int): Resource<Unit> {
        return try {
            val pedido = pedidoDao.getById(pedidoId)
                ?: return Resource.Error("Pedido no encontrado")

            val estadoActual = EstadoPedido.fromString(pedido.estado)
            if (estadoActual == EstadoPedido.ENVIADO ||
                estadoActual == EstadoPedido.ENTREGADO ||
                estadoActual == EstadoPedido.CANCELADO) {
                return Resource.Error("No se puede cancelar un pedido en estado ${estadoActual.estado}")
            }

            pedidoDao.updateEstado(pedidoId, EstadoPedido.CANCELADO.estado)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al cancelar pedido")
        }
    }
}