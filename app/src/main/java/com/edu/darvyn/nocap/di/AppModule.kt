package com.edu.darvyn.nocap.di

import android.content.Context
import androidx.room.Room
import com.edu.darvyn.nocap.data.local.database.NoCapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideNoCapDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
                appContext,
                NoCapDatabase::class.java,
                "NoCap.db"
            ).fallbackToDestructiveMigration(false)
            .build()

    @Provides
    @Singleton
    fun provideCategoria(noCapDb: NoCapDatabase) = noCapDb.categoriaDao()

    @Provides
    @Singleton
    fun provideMarcas(noCapDb: NoCapDatabase) = noCapDb.marcaDao()

    @Provides
    @Singleton
    fun provideUsuario(noCapDb: NoCapDatabase) = noCapDb.usuarioDao()
    @Provides
    @Singleton
    fun provideProducto(noCapDb: NoCapDatabase) = noCapDb.productoDao()

    @Provides
    @Singleton
    fun provideCarrito(noCapDb: NoCapDatabase) = noCapDb.carritoDao()

    @Provides
    @Singleton
    fun provideOrdenCompra(noCapDb: NoCapDatabase) = noCapDb.ordenCompraDao()

    @Provides
    @Singleton
    fun providePedido(noCapDb: NoCapDatabase) = noCapDb.pedidoDao()

    @Provides
    @Singleton
    fun providePagos(noCapDb: NoCapDatabase) = noCapDb.pagosDao()
}