package com.edu.darvyn.nocap.domain.useCase.useCaseCategoria

data class ValidationResult (
    val isValid: Boolean,
    val error: String? = null
)

fun validateNombreCategoria(nombre: String) : ValidationResult {
    nombre.trim().lowercase()
    if (nombre.isBlank())
        return ValidationResult(false, "El nombre de la categoria es obligatirio.")

    if (nombre.length < 3)
        return ValidationResult(false, "El minimo son 3 letras.")

    return ValidationResult(true)
}

fun validateDescripcionCategoria(descripcion: String) : ValidationResult {
    descripcion.trim()
    if (descripcion.isBlank())
        return ValidationResult(false, "La descripcion no puede estar vacia.")

    if (descripcion.length < 10)
        return ValidationResult(false, "La descripcion debe de tener mas de 10 palabras")

    return ValidationResult(true)
}