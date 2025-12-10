package com.edu.darvyn.nocap.domain.useCase.useCaseOrdenCompra

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.OrdenCompra
import com.edu.darvyn.nocap.domain.repository.OrdenCompraRepository
import javax.inject.Inject

class CrearOrdenUseCase @Inject constructor(
    private val repository: OrdenCompraRepository
) {
    suspend operator fun invoke(
        carritoId: Int,
        usuarioId: Int,
        pagarAhora: Boolean
    ): Resource<OrdenCompra> {
        // Validaciones
        if (carritoId <= 0) {
            return Resource.Error("Carrito inválido")
        }

        if (usuarioId <= 0) {
            return Resource.Error("Usuario inválido")
        }

        // Si es pago diferido, verificar límite
        if (!pagarAhora) {
            val pagosPendientes = repository.countPagosPendientes(usuarioId)
            if (pagosPendientes >= 3) {
                return Resource.Error("Has alcanzado el límite de pagos pendientes (3 máximo)")
            }
        }

        return repository.crearOrden(carritoId, usuarioId, pagarAhora)
    }
}