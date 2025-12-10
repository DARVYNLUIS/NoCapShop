package com.edu.darvyn.nocap.domain.useCase.useCaseProducto

import com.edu.darvyn.nocap.domain.useCase.useCaseCategoria.ValidationResult

fun validateNombreProducto(nombre: String): ValidationResult {
    val value = nombre.trim()
    if (value.isBlank())
        return ValidationResult(false, "El nombre del producto es obligatorio.")

    if (value.length < 3)
        return ValidationResult(false, "El nombre del producto debe tener al menos 3 caracteres.")

    return ValidationResult(true)
}

fun validateDescripcionProducto(descripcion: String): ValidationResult {
    val value = descripcion.trim()
    if (value.isBlank())
        return ValidationResult(false, "La descripción del producto es obligatoria.")

    if (value.length < 10)
        return ValidationResult(false, "La descripción debe tener al menos 10 caracteres.")

    return ValidationResult(true)
}

fun validateFechaCreacionProducto(fecha: String): ValidationResult {
    val value = fecha.trim()
    if (value.isBlank())
        return ValidationResult(false, "La fecha de creación es obligatoria.")

    // Aquí puedes meter validaciones de formato (YYYY-MM-DD) si tu API lo requiere
    val regex = Regex("""\d{4}-\d{2}-\d{2}""")
    if (!regex.matches(value))
        return ValidationResult(false, "La fecha debe tener el formato YYYY-MM-DD.")

    return ValidationResult(true)
}

fun validateProductoImagen(imagen: String): ValidationResult {
    val value = imagen.trim()
    if (value.isBlank())
        return ValidationResult(false, "La imagen del producto es obligatoria.")

    // Validar si parece una URL o un path válido
    if (!value.startsWith("http") && !value.contains("/"))
        return ValidationResult(false, "La imagen debe ser una URL o ruta válida.")

    return ValidationResult(true)
}

fun validatePrecioCompra(precio: Double): ValidationResult {
    if (precio <= 0.0)
        return ValidationResult(false, "El precio de compra debe ser mayor que 0.")

    return ValidationResult(true)
}

fun validatePrecioVenta(precio: Double): ValidationResult {
    if (precio <= 0.0)
        return ValidationResult(false, "El precio de venta debe ser mayor que 0.")

    return ValidationResult(true)
}

fun validateCategoriaId(categoriaId: Int): ValidationResult {
    if (categoriaId <= 0)
        return ValidationResult(false, "Debe seleccionar una categoría válida.")

    return ValidationResult(true)
}

fun validateMarcaId(marcaId: Int): ValidationResult {
    if (marcaId <= 0)
        return ValidationResult(false, "Debe seleccionar una marca válida.")

    return ValidationResult(true)
}