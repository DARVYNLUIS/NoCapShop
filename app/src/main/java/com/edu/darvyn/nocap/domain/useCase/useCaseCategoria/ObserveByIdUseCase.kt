package com.edu.darvyn.nocap.domain.useCase.useCaseCategoria

import com.edu.darvyn.nocap.domain.repository.CategoriaRepository
import javax.inject.Inject

class ObserveByIdUseCase @Inject constructor(
    private val categoriaRepository: CategoriaRepository
){
    suspend operator fun invoke(id: Int) =
        categoriaRepository.observeById(id)
}
