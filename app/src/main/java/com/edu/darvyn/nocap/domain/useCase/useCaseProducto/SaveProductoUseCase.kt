package com.edu.darvyn.nocap.domain.useCase.useCaseProducto

import com.edu.darvyn.nocap.domain.model.Producto
import com.edu.darvyn.nocap.domain.repository.ProductoRepository
import javax.inject.Inject

class SaveProductoUseCase @Inject constructor(
    private val productoRepository: ProductoRepository
) {
    suspend operator fun invoke(producto: Producto): Result<Unit> {


        return runCatching { productoRepository.saveProducto(producto) }
    }
}
