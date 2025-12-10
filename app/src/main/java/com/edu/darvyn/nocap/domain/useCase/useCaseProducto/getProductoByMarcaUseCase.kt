package com.edu.darvyn.nocap.domain.useCase.useCaseProducto

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Producto
import com.edu.darvyn.nocap.domain.repository.ProductoRepository
import javax.inject.Inject

class getProductoByMarcaUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke (marcaIds: List<Int>?) : Resource<List<Producto>>{
        return repository.getProductosByMarcas(marcaIds)
    }
}