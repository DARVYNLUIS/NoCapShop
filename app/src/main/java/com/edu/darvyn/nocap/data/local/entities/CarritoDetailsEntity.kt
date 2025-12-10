package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "CarritoDetailsEntities",
        foreignKeys = [
    ForeignKey(
        entity = CarritoEntity::class,
        parentColumns = ["carritoId"],
        childColumns = ["carritoId"],
        onDelete = ForeignKey.CASCADE
    )
],
    indices = [Index("carritoId")]
)
data class CarritoDetailsEntity(
    @PrimaryKey
    val carritoDetailsId: Int?,
    val carritoId: Int?,
    val productoId: Int,
    val cantidad: Int,
    val precioProducto: Double,
    val color: String,
    val talla: String
)
