package com.edu.darvyn.nocap.domain.useCase.useCaseCategoria

import com.edu.darvyn.nocap.domain.model.Categoria
import com.edu.darvyn.nocap.domain.repository.CategoriaRepository
import javax.inject.Inject

class SaveCategoriaUseCase @Inject constructor(
    private val categoriaRepository: CategoriaRepository
){
   suspend operator fun invoke(categoria : Categoria) : Result<Unit> {
        val nombreResult = validateNombreCategoria(categoria.nombre)
        if (!nombreResult.isValid)
            return Result.failure(IllegalArgumentException(nombreResult.error))

        val categoriaResult = validateDescripcionCategoria(categoria.descripcion)
        if (!categoriaResult.isValid)
            return Result.failure(IllegalArgumentException(categoriaResult.error))

        return runCatching { categoriaRepository.save(categoria) }
    }
}