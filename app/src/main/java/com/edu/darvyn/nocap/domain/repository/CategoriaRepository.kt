package com.edu.darvyn.nocap.domain.repository

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Categoria
import kotlinx.coroutines.flow.Flow

interface CategoriaRepository {
    fun observeAll(): Flow<Resource<List<Categoria>>>
    suspend fun save(categoria : Categoria)
    suspend fun observeById(id: Int) : Categoria
    suspend fun deleteById(id: Int)
}