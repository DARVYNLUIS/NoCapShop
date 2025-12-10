package com.edu.darvyn.nocap.domain.repository

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun observeAllProductos() : Flow<Resource<List<Producto>>>
    suspend fun saveProducto(producto: Producto)
    suspend fun observeProductoById (id: Int) : Producto
    suspend fun deleteProducto(id : Int)
    suspend fun getProductosByMarcas(marcaIds: List<Int>?): Resource<List<Producto>>

}