package com.edu.darvyn.nocap.data.repository

import com.edu.darvyn.nocap.data.local.dao.MarcasDao
import com.edu.darvyn.nocap.data.local.mappers.dtoToEntity
import com.edu.darvyn.nocap.data.local.mappers.toDomain
import com.edu.darvyn.nocap.data.local.mappers.toDto
import com.edu.darvyn.nocap.data.local.mappers.toEntity
import com.edu.darvyn.nocap.data.remote.RemoteDataSource
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.model.Marcas
import com.edu.darvyn.nocap.domain.repository.MarcasRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarcasRepositoryImpl @Inject constructor(
    private val marcasDao: MarcasDao,
    private val remoteDataSource: RemoteDataSource
): MarcasRepository {

    override fun observeAll(): Flow<Resource<List<Marcas>> >{
        return flow {
            emit(Resource.Loading())
            try {
                var marcaRemote = remoteDataSource.observeAllMarcas()
                if (marcaRemote.isNotEmpty()) {
                    marcasDao.deleteAll()
                    marcasDao.saveList(marcaRemote.map { it.dtoToEntity() })
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Error"))
            }
            emitAll(
                marcasDao.getAll()
                    .map { lista -> lista.map { it.toDomain() } }
                    .map { Resource.Success(it) }
            )
        }
    }

    override suspend fun observeById(id: Int): Marcas {
        var categoria = marcasDao.getMarcaById(id)
        return  categoria.toDomain()
    }

    override suspend fun save(marcas: Marcas) {
        try {
            remoteDataSource.saveMarcas(marcas.toDto())

        }catch (e: Exception){
            Resource.Error(e.message ?: "Error", null)
        }
        marcasDao.save(marcas.toEntity())    }

    override suspend fun deleteById(id: Int) {
        try {
            remoteDataSource.eliminarMarca(id)
        }catch (e: Exception){
            Resource.Error(e.message ?: "Error", null)
        }
        marcasDao.deleteById(id)

    }


}