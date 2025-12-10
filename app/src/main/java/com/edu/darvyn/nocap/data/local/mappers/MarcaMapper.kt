package com.edu.darvyn.nocap.data.local.mappers

import com.edu.darvyn.nocap.data.local.entities.MarcasEntity
import com.edu.darvyn.nocap.data.remote.dto.MarcasDto
import com.edu.darvyn.nocap.domain.model.Marcas

fun MarcasEntity.toDomain(): Marcas = Marcas(
    marcaId = marcaId,
    nombre = nombre,
    activa = activa
)

fun Marcas.toEntity(): MarcasEntity = MarcasEntity(
    marcaId = marcaId,
    nombre = nombre,
    activa = activa
)

fun Marcas.toDto() : MarcasDto = MarcasDto(
    marcaId = marcaId,
    nombre = nombre,
    activo = activa
)

fun MarcasDto.dtoToEntity() : MarcasEntity = MarcasEntity(
    marcaId = marcaId,
    nombre = nombre,
    activa = activo
)