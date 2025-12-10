package com.edu.darvyn.nocap.domain.useCase.useCaseCarritos

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.repository.CarritoRepository
import javax.inject.Inject

class EliminarItemCarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(carritoDetailsId: Int): Resource<Unit> {
        if (carritoDetailsId <= 0) {
            return Resource.Error("ID de item inválido")
        }
        return repository.eliminarItem(carritoDetailsId)
    }
}