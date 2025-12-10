package com.edu.darvyn.nocap.domain.useCase.useCaseProducto

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Producto
import com.edu.darvyn.nocap.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAllProductoUseCase @Inject constructor(
    private val productoRepository: ProductoRepository
) {
    operator fun invoke() : Flow<Resource<List<Producto>>> =
        productoRepository.observeAllProductos()
}