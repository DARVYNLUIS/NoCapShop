package com.edu.darvyn.nocap.domain.useCase.useCaseCarritos

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.repository.CarritoRepository
import javax.inject.Inject

class ActualizarCantidadUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(carritoDetailsId: Int, cantidad: Int): Resource<Unit> {
        if (cantidad < 0) {
            return Resource.Error("La cantidad no puede ser negativa")
        }
        if (carritoDetailsId <= 0) {
            return Resource.Error("ID de item inválido")
        }
        return repository.actualizarCantidad(carritoDetailsId, cantidad)
    }
}