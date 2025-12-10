package com.edu.darvyn.nocap.domain.useCase.useCaseMarcas

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Marcas
import com.edu.darvyn.nocap.domain.repository.MarcasRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAllMarcasUseCase @Inject constructor(
    private val marcasRepository: MarcasRepository
){
    operator fun invoke() : Flow<Resource<List<Marcas>>> =
        marcasRepository.observeAll()
}