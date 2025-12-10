package com.edu.darvyn.nocap.domain.useCase.useCasePedidos

import com.edu.darvyn.nocap.domain.model.Pedido
import com.edu.darvyn.nocap.domain.repository.PedidoRepository
import javax.inject.Inject

class GetAllPedidosUseCase @Inject constructor(
    private val repository: PedidoRepository
) {

    suspend operator fun invoke(): List<Pedido?> {
        return repository.getAllPedidos()
    }
}