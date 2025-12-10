package com.edu.darvyn.nocap.domain.useCase.useCaseMarcas

import com.edu.darvyn.nocap.domain.repository.MarcasRepository
import javax.inject.Inject

class ObserveMarcaByIdUseCase @Inject constructor(
    private val marcasRepository: MarcasRepository
) {
    suspend operator fun invoke(id: Int) =
        marcasRepository.observeById(id)
}