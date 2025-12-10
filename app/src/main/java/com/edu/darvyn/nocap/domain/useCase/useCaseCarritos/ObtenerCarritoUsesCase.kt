package com.edu.darvyn.nocap.domain.useCase.useCaseCarritos

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Carrito
import com.edu.darvyn.nocap.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetCarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    operator fun invoke(usuarioId: Int): Flow<Resource<Carrito>> {
        return repository.observeCarrito(usuarioId)
    }
}
