package com.edu.darvyn.nocap.data.local.mappers

import com.edu.darvyn.nocap.data.local.entities.CategoriasEntity
import com.edu.darvyn.nocap.data.remote.dto.CategoriaDto
import com.edu.darvyn.nocap.domain.model.Categoria

fun CategoriasEntity.toDomain(): Categoria = Categoria(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    activa = activa
)

fun Categoria.toEntity(): CategoriasEntity = CategoriasEntity(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    activa = activa
)

fun Categoria.domainToDto(): CategoriaDto = CategoriaDto(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    activo = activa
)

fun CategoriaDto.dtoToEntity(): CategoriasEntity = CategoriasEntity(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    activa = activo
)

