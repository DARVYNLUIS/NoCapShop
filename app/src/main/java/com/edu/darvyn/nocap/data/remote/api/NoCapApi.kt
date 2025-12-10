package com.edu.darvyn.nocap.data.remote.api

import com.edu.darvyn.nocap.data.remote.dto.CategoriaDto
import com.edu.darvyn.nocap.data.remote.dto.MarcasDto
import com.edu.darvyn.nocap.data.remote.dto.ProductosDto
import com.edu.darvyn.nocap.data.remote.dto.RequestLogin
import com.edu.darvyn.nocap.data.remote.dto.UsuarioDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface NoCapApi {
    //Categoria
    @GET("api/Categorias/Lista")
    suspend fun observeAllCategorias() : List<CategoriaDto>

    @POST("api/Categorias/Agregar")
    suspend fun saveCategoria(@Body categoriaDto: CategoriaDto) : Boolean

    @PUT("api/Categorias/Editar/{id}")
    suspend fun editCategoria(@Path("id") id: Int, @Body categoriaDto: CategoriaDto  ) :  Boolean

    @DELETE("api/Categorias/Eliminar/{id}")
    suspend fun eliminarCategoria(@Path("id") id: Int) : Boolean

    //Marca
    @GET("api/Marcas/Lista")
    suspend fun observeAllMarcas() : List<MarcasDto>
    @POST("api/Marcas/Agregar")
    suspend fun saveMarca(@Body marcaDto: MarcasDto) : Boolean
    @PUT("api/Marcas/Editar/{id}")
    suspend fun editMarca(@Path("id") id: Int, @Body marcasDto: MarcasDto ) : Boolean
    @DELETE("/api/Marcas/Eliminar/{id}")
    suspend fun deleteMarca(@Path("id") id: Int) : Boolean

    //Usuario
    @POST("api/Usuarios/IniciarSesion")
    suspend fun iniciarSesion(@Body request : RequestLogin) : UsuarioDto
    @POST("api/Usuarios/CrearUsuario")
    suspend fun crearUsuario(@Body usuarioDto: UsuarioDto)

    //Productos
    @GET("api/Productos/Listar")
    suspend fun observeAllProductos() : List<ProductosDto>
    @POST("/api/Productos/Guardar")
    suspend fun saveProducto(@Body productos : ProductosDto)
    @DELETE("api/Productos/Eliminar/{id}")
    suspend fun deleteProducto(@Path("id") id: Int) : Boolean

}
