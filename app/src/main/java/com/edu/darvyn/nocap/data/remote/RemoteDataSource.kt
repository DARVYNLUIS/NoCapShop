package com.edu.darvyn.nocap.data.remote

import com.edu.darvyn.nocap.data.remote.api.NoCapApi
import com.edu.darvyn.nocap.data.remote.dto.CategoriaDto
import com.edu.darvyn.nocap.data.remote.dto.MarcasDto
import com.edu.darvyn.nocap.data.remote.dto.ProductosDto
import com.edu.darvyn.nocap.data.remote.dto.RequestLogin
import com.edu.darvyn.nocap.data.remote.dto.UsuarioDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val noCapApi: NoCapApi
) {
    //Categoria
    suspend fun observeAllCategorias() = noCapApi.observeAllCategorias()
    suspend fun saveCategoria(categoriaDto: CategoriaDto) = noCapApi.saveCategoria(categoriaDto)
    suspend fun editCategoria(id: Int, categoriaDto: CategoriaDto) = noCapApi.editCategoria(id, categoriaDto)
    suspend fun eliminarCategoria(id: Int) = noCapApi.eliminarCategoria(id)

    //Marcas
    suspend fun observeAllMarcas() = noCapApi.observeAllMarcas()
    suspend fun saveMarcas(marcasDto: MarcasDto) = noCapApi.saveMarca(marcasDto)
    suspend fun editMarca(id: Int, marcasDto: MarcasDto) = noCapApi.editMarca(id,marcasDto)
    suspend fun eliminarMarca (id: Int) = noCapApi.deleteMarca(id)

    //Usuario
    suspend fun iniciarSesion(request : RequestLogin) = noCapApi.iniciarSesion(request)
    suspend fun crearSesion(usuarioDto: UsuarioDto) = noCapApi.crearUsuario(usuarioDto)

    //Producto
    suspend fun observeAllProductos() = noCapApi.observeAllProductos()
    suspend fun saveProducto(producto: ProductosDto) = noCapApi.saveProducto(producto)
    suspend fun deleteProducto(id: Int) = noCapApi.deleteProducto(id)
}