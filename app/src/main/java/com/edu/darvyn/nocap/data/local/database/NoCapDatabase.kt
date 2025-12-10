package com.edu.darvyn.nocap.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.edu.darvyn.nocap.data.local.converters.Converters
import com.edu.darvyn.nocap.data.local.dao.CarritoDao
import com.edu.darvyn.nocap.data.local.dao.CategoriaDao
import com.edu.darvyn.nocap.data.local.dao.MarcasDao
import com.edu.darvyn.nocap.data.local.dao.OrdenCompraDao
import com.edu.darvyn.nocap.data.local.dao.PagosDao
import com.edu.darvyn.nocap.data.local.dao.PedidoDao
import com.edu.darvyn.nocap.data.local.dao.ProductoDao
import com.edu.darvyn.nocap.data.local.dao.UsuarioDao
import com.edu.darvyn.nocap.data.local.entities.CarritoDetailsEntity
import com.edu.darvyn.nocap.data.local.entities.CarritoEntity
import com.edu.darvyn.nocap.data.local.entities.CategoriasEntity
import com.edu.darvyn.nocap.data.local.entities.EstadosEntity
import com.edu.darvyn.nocap.data.local.entities.MarcasEntity
import com.edu.darvyn.nocap.data.local.entities.OrdenCompraEntity
import com.edu.darvyn.nocap.data.local.entities.PagosEntity
import com.edu.darvyn.nocap.data.local.entities.PedidosEntity
import com.edu.darvyn.nocap.data.local.entities.ProductoEntity
import com.edu.darvyn.nocap.data.local.entities.UsuarioEntity

@Database(
    entities = [
        CarritoEntity::class,
        CarritoDetailsEntity::class,
        CategoriasEntity::class,
        EstadosEntity::class,
        MarcasEntity::class,
        OrdenCompraEntity::class,
        PagosEntity::class,
        ProductoEntity::class,
        UsuarioEntity::class,
        PedidosEntity::class
    ],
    version = 15,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NoCapDatabase : RoomDatabase() {
    abstract fun usuarioDao() : UsuarioDao
    abstract fun carritoDao(): CarritoDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun marcaDao(): MarcasDao
    abstract fun ordenCompraDao(): OrdenCompraDao
    abstract fun pagosDao (): PagosDao
    abstract fun productoDao(): ProductoDao
    abstract  fun pedidoDao(): PedidoDao
}