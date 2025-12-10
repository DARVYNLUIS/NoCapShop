package com.edu.darvyn.nocap.domain.useCase.useCaseMarcas

import com.edu.darvyn.nocap.domain.model.Marcas
import com.edu.darvyn.nocap.domain.repository.MarcasRepository
import javax.inject.Inject

class SaveMarcaUseCase @Inject constructor(
    private val marcasRepository: MarcasRepository
) {
    suspend operator fun invoke(marca : Marcas) : Result<Unit> {
        val nombreResult = validateNombreMarca(marca.nombre)
        if (!nombreResult.isValid)
            return Result.failure(IllegalArgumentException(nombreResult.error))

        return runCatching { marcasRepository.save(marca) }
    }
}