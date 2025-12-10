package com.edu.darvyn.nocap.domain.useCase.useCaseCarritos

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.repository.CarritoRepository
import javax.inject.Inject

class AgregarProductoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(
        usuarioId: Int,
        productoId: Int,
        cantidad: Int,
        precio: Double,
        color: String,
        talla: String
    ): Resource<Unit> {

        if (cantidad <= 0) {
            return Resource.Error("La cantidad debe ser mayor a 0")
        }
        if (precio <= 0) {
            return Resource.Error("El precio debe ser mayor a 0")
        }


        return repository.agregarProducto(
            usuarioId = usuarioId,
            productoId = productoId,
            cantidad = cantidad,
            precio = precio,
            color = color,
            talla = talla
        )
    }
}