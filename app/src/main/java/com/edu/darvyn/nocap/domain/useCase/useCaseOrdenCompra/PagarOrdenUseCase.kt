package com.edu.darvyn.nocap.domain.useCase.useCaseOrdenCompra

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.repository.OrdenCompraRepository
import javax.inject.Inject

class PagarOrdenUseCase @Inject constructor(
    private val repository: OrdenCompraRepository
) {
    suspend operator fun invoke(ordenId: Int): Resource<Unit> {
        if (ordenId <= 0) {
            return Resource.Error("Orden inválida")
        }
        return repository.marcarComoPagado(ordenId)
    }
}