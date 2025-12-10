package com.edu.darvyn.nocap.domain.repository

import com.edu.darvyn.nocap.data.remote.dto.RequestLogin
import com.edu.darvyn.nocap.data.remote.dto.UsuarioDto
import com.edu.darvyn.nocap.domain.model.Usuario

interface UsuarioRepository {
    suspend fun iniciarSesion(request : RequestLogin) : Usuario?
    suspend fun crearUsuario(usuario: UsuarioDto)
    suspend fun getUsuario() : Usuario?
    suspend fun deleteAll()
}