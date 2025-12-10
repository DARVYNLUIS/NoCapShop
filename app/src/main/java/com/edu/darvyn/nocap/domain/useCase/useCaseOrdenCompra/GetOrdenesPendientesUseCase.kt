package com.edu.darvyn.nocap.domain.useCase.useCaseOrdenCompra

import com.edu.darvyn.nocap.domain.repository.OrdenCompraRepository
import javax.inject.Inject

class GetOrdenesPendientesUseCase @Inject constructor(
    private val repository: OrdenCompraRepository
) {
    operator fun invoke(usuarioId: Int) = repository.observePendientesByUsuario(usuarioId)
}