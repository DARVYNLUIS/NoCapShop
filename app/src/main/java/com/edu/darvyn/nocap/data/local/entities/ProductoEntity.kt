package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductoEntities")
data class ProductoEntity (
    @PrimaryKey
    val productoId : Int?,
    val nombre: String,
    val descripcion: String,
    val fechaCreacion: String?,
    val stocks: Int,
    val productoImagen: String?,
    val precioVenta: Double,
    val categoriaId: Int,
    val marcaId: Int,
    val activo: Boolean,
    val listaTamanos: List<String>,
    val listaColores: List<String>
)