package com.edu.darvyn.nocap.data.local.mappers

import com.edu.darvyn.nocap.data.local.entities.UsuarioEntity
import com.edu.darvyn.nocap.data.remote.dto.UsuarioDto
import com.edu.darvyn.nocap.domain.model.Usuario

fun UsuarioEntity.toDomain(): Usuario? = Usuario(
    usuarioId = usuarioId,
    nombres = nombres,
    email = email,
    password = null,
    confirmPassword = null,
    rol = rol
)


fun UsuarioDto.dtoToEntity() : UsuarioEntity = UsuarioEntity(
    usuarioId = usuarioId,
    nombres = nombres,
    email = correo,
    rol = rolId ?: 2
)

fun Usuario.domainToDto() : UsuarioDto = UsuarioDto(
    usuarioId = usuarioId,
    nombres = nombres,
    correo = email,
    contrasena = password,
    rolId = rol
)