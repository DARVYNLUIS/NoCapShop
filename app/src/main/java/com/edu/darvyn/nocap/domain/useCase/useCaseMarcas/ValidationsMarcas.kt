package com.edu.darvyn.nocap.domain.useCase.useCaseMarcas

data class ValidationResult (
    val isValid: Boolean,
    val error: String? = null
)

fun validateNombreMarca(nombre: String) : ValidationResult {
    nombre.trim().lowercase()
    if (nombre.isBlank())
        return ValidationResult(false, "El nombre de la categoria es obligatirio.")

    if (nombre.length < 3)
        return ValidationResult(false, "El minimo son 3 letras.")

    return ValidationResult(true)
}
