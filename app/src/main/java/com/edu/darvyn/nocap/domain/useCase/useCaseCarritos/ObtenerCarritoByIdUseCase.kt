package com.edu.darvyn.nocap.domain.useCase.useCaseCarritos

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Carrito
import com.edu.darvyn.nocap.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObtenerCarritoByIdUseCase@Inject constructor(
    private val repository: CarritoRepository
) {
    operator fun invoke(carritoId: Int): Flow<Resource<Carrito>> {
        return repository.observeCarrito(carritoId)
    }
}