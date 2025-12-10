package com.edu.darvyn.nocap.domain.repository

import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Marcas
import kotlinx.coroutines.flow.Flow

interface MarcasRepository {
    fun observeAll() : Flow<Resource<List<Marcas>>>
    suspend fun observeById(id : Int) : Marcas
    suspend fun save(marcas: Marcas)
    suspend fun deleteById(id: Int)
}