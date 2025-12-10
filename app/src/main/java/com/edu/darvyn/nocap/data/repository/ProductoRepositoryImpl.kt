package com.edu.darvyn.nocap.data.repository

import com.edu.darvyn.nocap.data.local.dao.ProductoDao
import com.edu.darvyn.nocap.data.local.mappers.dtoToEntity
import com.edu.darvyn.nocap.data.local.mappers.toDomain
import com.edu.darvyn.nocap.data.local.mappers.toDto
import com.edu.darvyn.nocap.data.local.mappers.toEntity
import com.edu.darvyn.nocap.data.remote.RemoteDataSource
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Producto
import com.edu.darvyn.nocap.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    private val productoDao: ProductoDao,
    private val remoteDataSource: RemoteDataSource
): ProductoRepository {
    override fun observeAllProductos(): Flow<Resource<List<Producto>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val productoRemote = remoteDataSource.observeAllProductos()
                if (productoRemote.isNotEmpty()) {
                    productoDao.deleteAll()
                    productoDao.saveListProductos(productoRemote.map { it.dtoToEntity() })
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
            emitAll(
                productoDao.observeAllProductos()
                    .map { list -> list.map { it.toDomain() } }
                    .map { Resource.Success(it) }
            )
        }
    }

    override suspend fun saveProducto(producto: Producto) {
        try {
            remoteDataSource.saveProducto(producto.toDto())

        }catch (e: Exception){
            Resource.Error(e.message ?: "Error", null)
        }
        productoDao.saveProducto(producto.toEntity())
    }

    override suspend fun observeProductoById(id: Int): Producto{
        val producto = productoDao.observeById(id)
        return  producto.toDomain()
    }

    override suspend fun getProductosByMarcas(marcaIds: List<Int>?): Resource<List<Producto>> {
        return try {
            val productos = if (marcaIds.isNullOrEmpty()) {
                productoDao.getAllProductos()
            } else {
                productoDao.getProductosByMarcas(marcaIds)
            }
            Resource.Success(productos.map { it.toDomain() })
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al filtrar productos")
        }
    }

    override suspend fun deleteProducto(id: Int) {
        try {
            remoteDataSource.deleteProducto(id)
        }catch (e: Exception){
            Resource.Error(e.message ?: "Error", null)
        }
        productoDao.deleteById(id)
    }
}