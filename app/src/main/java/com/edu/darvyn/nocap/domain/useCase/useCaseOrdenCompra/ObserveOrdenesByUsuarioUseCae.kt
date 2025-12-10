package com.edu.darvyn.nocap.domain.useCase.useCaseOrdenCompra

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.OrdenCompra
import com.edu.darvyn.nocap.domain.repository.OrdenCompraRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveOrdenesByUsuarioUseCae @Inject constructor(
    private val repository: OrdenCompraRepository
) {
     operator fun invoke(usuarioId: Int): Flow<Resource<List<OrdenCompra>>> {

        return repository.observeByUsuario(usuarioId)
    }
}