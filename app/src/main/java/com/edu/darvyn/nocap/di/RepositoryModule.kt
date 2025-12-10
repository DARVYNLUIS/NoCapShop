package com.edu.darvyn.nocap.di

import com.edu.darvyn.nocap.data.repository.CarritoRepositoryImpl
import com.edu.darvyn.nocap.data.repository.CategoriaRepositoryImpl
import com.edu.darvyn.nocap.data.repository.MarcasRepositoryImpl
import com.edu.darvyn.nocap.data.repository.OrdenCompraRepositoryImpl
import com.edu.darvyn.nocap.data.repository.PedidoRepositoryImpl
import com.edu.darvyn.nocap.data.repository.ProductoRepositoryImpl
import com.edu.darvyn.nocap.data.repository.UsuarioRepositoryImpl
import com.edu.darvyn.nocap.domain.repository.CarritoRepository
import com.edu.darvyn.nocap.domain.repository.CategoriaRepository
import com.edu.darvyn.nocap.domain.repository.MarcasRepository
import com.edu.darvyn.nocap.domain.repository.OrdenCompraRepository
import com.edu.darvyn.nocap.domain.repository.PedidoRepository
import com.edu.darvyn.nocap.domain.repository.ProductoRepository
import com.edu.darvyn.nocap.domain.repository.UsuarioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCategoriaRepository(
        categoriaRepositoryImpl: CategoriaRepositoryImpl
    ): CategoriaRepository

    @Binds
    @Singleton
    abstract fun bindMarcasRepository(
        marcasRepositoryImpl: MarcasRepositoryImpl
    ): MarcasRepository

    @Binds
    @Singleton
    abstract fun bindUsuarioRepository(
        usuarioRepositoryImpl: UsuarioRepositoryImpl
    ): UsuarioRepository

    @Binds
    @Singleton
    abstract fun bindProductoRepository(
        productoRepository: ProductoRepositoryImpl
    ): ProductoRepository

    @Binds
    @Singleton
    abstract fun bindCarritoRepository(
        carritoRepository: CarritoRepositoryImpl
    ): CarritoRepository

    @Binds
    @Singleton
    abstract fun bindOrdenRepositoryRepository(
        ordenRepository: OrdenCompraRepositoryImpl
    ): OrdenCompraRepository

    @Binds
    @Singleton
    abstract fun bindPedidosRepository(
        pedidoRepository: PedidoRepositoryImpl
    ): PedidoRepository


}