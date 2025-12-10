package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CarritoDetailWithProducto(
    @Embedded val detail: CarritoDetailsEntity,
    @Relation(
        parentColumn = "productoId",
        entityColumn = "productoId"
    )
    val producto: ProductoEntity?
)