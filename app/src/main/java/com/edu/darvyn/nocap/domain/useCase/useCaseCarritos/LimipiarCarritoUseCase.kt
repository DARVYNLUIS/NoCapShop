package com.edu.darvyn.nocap.domain.useCase.useCaseCarritos

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.repository.CarritoRepository
import javax.inject.Inject

class LimpiarCarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(carritoId: Int): Resource<Unit> {
        if (carritoId <= 0) {
            return Resource.Error("ID de carrito inválido")
        }
        return repository.limpiarCarrito(carritoId)
    }
}