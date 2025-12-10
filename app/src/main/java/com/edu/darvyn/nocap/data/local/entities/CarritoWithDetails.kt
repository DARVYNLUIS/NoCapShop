package com.edu.darvyn.nocap.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class CarritoWithDetails(
    @Embedded val carrito: CarritoEntity,
    @Relation(
        parentColumn = "carritoId",
        entityColumn = "carritoId"
    )
    val details: List<CarritoDetailsEntity>
)