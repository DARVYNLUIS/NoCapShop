package com.edu.darvyn.nocap.data.repository

import com.edu.darvyn.nocap.data.local.dao.CategoriaDao
import com.edu.darvyn.nocap.data.local.mappers.domainToDto
import com.edu.darvyn.nocap.data.local.mappers.dtoToEntity
import com.edu.darvyn.nocap.data.local.mappers.toDomain
import com.edu.darvyn.nocap.data.local.mappers.toEntity
import com.edu.darvyn.nocap.data.remote.RemoteDataSource
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Categoria
import com.edu.darvyn.nocap.domain.repository.CategoriaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class CategoriaRepositoryImpl @Inject constructor(
    private val categoriaDao: CategoriaDao,
    private val remoteDataSource: RemoteDataSource
) : CategoriaRepository {

    override fun observeAll(): Flow<Resource<List<Categoria>>> {
        return flow {
            emit(Resource.Loading())
            try {
                var categoriaRemote = remoteDataSource.observeAllCategorias()
                if (categoriaRemote.isNotEmpty()) {
                    categoriaDao.deleteAll()
                    categoriaDao.saveList(categoriaRemote.map { it.dtoToEntity() })
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
            emitAll(
                categoriaDao.getAll()
                    .map { lista -> lista.map { it.toDomain() } }
                    .map { Resource.Success(it) }
            )
        }
    }

    override suspend fun save(categoria: Categoria) {
        try {
            remoteDataSource.saveCategoria(categoria.domainToDto())

        }catch (e: Exception){
            Resource.Error(e.message ?: "Error", null)
        }
        categoriaDao.save(categoria.toEntity())
    }

    override suspend fun observeById(id: Int): Categoria {
        var categoria = categoriaDao.getCategoria(id)
        return  categoria.toDomain()
    }

    override suspend fun deleteById(id: Int) {
        try {
            remoteDataSource.eliminarCategoria(id)
        }catch (e: Exception){
            Resource.Error(e.message ?: "Error", null)
        }
        categoriaDao.eliminarCategoria(id)
    }
}