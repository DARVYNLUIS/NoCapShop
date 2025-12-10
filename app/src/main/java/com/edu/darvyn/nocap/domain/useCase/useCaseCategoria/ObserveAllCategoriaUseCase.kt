package com.edu.darvyn.nocap.domain.useCase.useCaseCategoria

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Categoria
import com.edu.darvyn.nocap.domain.repository.CategoriaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAllCategoriaUseCase @Inject constructor(
    private val categoriaRepository: CategoriaRepository
) {
    operator fun invoke() : Flow<Resource<List<Categoria>>> =
        categoriaRepository.observeAll()
}
