package com.edu.darvyn.nocap.domain.useCase.useCaseCarritos

import com.edu.darvyn.nocap.domain.model.Carrito
import javax.inject.Inject

class CalcularTotalesUseCase @Inject constructor() {
    data class Totales(
        val subtotal: Double,
        val envio: Double,
        val total: Double,
        val envioGratis: Boolean
    )

    operator fun invoke(carrito: Carrito): Totales {
        val subtotal = carrito.items.sumOf { it.subtotal }
        val envioGratis = subtotal >= 30.0
        val envio = if (envioGratis) 0.0 else 5.0
        val total = subtotal + envio

        return Totales(
            subtotal = subtotal,
            envio = envio,
            total = total,
            envioGratis = envioGratis
        )
    }
}
