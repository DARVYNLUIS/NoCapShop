package com.edu.darvyn.nocap.data.repository

import android.util.Log
import com.edu.darvyn.nocap.data.local.dao.UsuarioDao
import com.edu.darvyn.nocap.data.local.mappers.dtoToEntity
import com.edu.darvyn.nocap.data.local.mappers.toDomain
import com.edu.darvyn.nocap.data.remote.RemoteDataSource
import com.edu.darvyn.nocap.data.remote.dto.RequestLogin
import com.edu.darvyn.nocap.data.remote.dto.UsuarioDto
import com.edu.darvyn.nocap.domain.model.Usuario
import com.edu.darvyn.nocap.domain.repository.UsuarioRepository
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    private val usuarioDao: UsuarioDao,
    private val remoteDataSource : RemoteDataSource
): UsuarioRepository {

    override suspend fun iniciarSesion(request: RequestLogin)  : Usuario? {
        try {
            val dto = remoteDataSource.iniciarSesion(request)

            usuarioDao.save(dto.dtoToEntity())
        }catch (e : Exception){
            Log.e("UsuarioRepository", "Error inesperado: ${e.message}", e)
        }

        return getUsuario()
    }

    override suspend fun crearUsuario(usuario: UsuarioDto) {
        try {
            remoteDataSource.crearSesion(usuario)
        }catch (e: Exception) {
            Log.e("UsuarioRepository", "Error inesperado: ${e.message}", e)
        }

    }

    override suspend fun deleteAll() =
        usuarioDao.deleteAll()


    override suspend fun getUsuario(): Usuario? {
        val usuario = usuarioDao.getUsuario()
        return usuario?.toDomain()
    }


}