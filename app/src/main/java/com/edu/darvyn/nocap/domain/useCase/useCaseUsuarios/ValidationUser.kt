package com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios

import android.util.Patterns
import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.ValidationResult

fun validateEmailUser(email : String): ValidationResult {
    email.trim().lowercase()
    if (email.isBlank())
        return ValidationResult(false, "El email no puede estar vacio.")

    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return ValidationResult(false, "El email no es valido")

    return ValidationResult(true)

}

fun validateNombreUser(nombre: String) : ValidationResult {
    nombre.trim().lowercase()
    if (nombre.isBlank())
        return ValidationResult(false, "El nombre de la categoria es obligatirio.")

    if (nombre.length < 3)
        return ValidationResult(false, "El minimo son 3 letras.")

    return ValidationResult(true)
}

fun validateConfirmationPasswordUser(password: String, confirmPassword: String) : ValidationResult{
    password.trim().lowercase()
    confirmPassword.trim().lowercase()

    if (password != confirmPassword)
        return ValidationResult(false, "Las contraseñas no pueden ser diferentes ")

    return ValidationResult(true)
}

fun validatePasswordUser(password: String) : ValidationResult {
    password.trim().lowercase()
    if (password.isBlank())
        return ValidationResult(false, "la contraseña no puede estar vacia")

    if (password.length < 8)
        return ValidationResult(false, "la contraseña debe tener mas de 8 letras")

    return ValidationResult(true)

}