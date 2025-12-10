package com.edu.darvyn.nocap.domain.useCase.useCaseOrdenCompra

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.OrdenCompra
import com.edu.darvyn.nocap.domain.repository.OrdenCompraRepository
import javax.inject.Inject

class GetOrdenUseCase @Inject constructor(
    private val repository: OrdenCompraRepository
) {
    suspend  fun invoke(ordenId: Int): OrdenCompra {
        return repository.getById(ordenId)
    }
}