package com.edu.darvyn.nocap.domain.useCase.useCaseProducto

import com.edu.darvyn.nocap.domain.repository.ProductoRepository
import javax.inject.Inject

class DeleteProductoByIdUseCase @Inject constructor(
    private val productoRepository: ProductoRepository
) {
    suspend operator fun invoke(productoId: Int) =
        productoRepository.deleteProducto(productoId)
}