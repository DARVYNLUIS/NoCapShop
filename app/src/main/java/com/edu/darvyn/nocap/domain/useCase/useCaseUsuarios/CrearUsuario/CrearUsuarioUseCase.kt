package com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.CrearUsuario

import com.edu.darvyn.nocap.data.local.mappers.domainToDto
import com.edu.darvyn.nocap.data.remote.dto.RequestLogin
import com.edu.darvyn.nocap.domain.model.Usuario
import com.edu.darvyn.nocap.domain.repository.UsuarioRepository
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validateConfirmationPasswordUser
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validateEmailUser
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validateNombreUser
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validatePasswordUser
import javax.inject.Inject

class CrearUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(usuario: Usuario) : Result<Unit> {
        val emailResult = validateEmailUser(usuario.email)
        if (!emailResult.isValid)
            return Result.failure(IllegalArgumentException(emailResult.error))

        val passwordResult = validatePasswordUser(usuario.password!!)
        if (!passwordResult.isValid)
            return Result.failure(IllegalArgumentException(passwordResult.error))

        val nombreResult = validateNombreUser(usuario.email)
        if (!nombreResult.isValid)
            return Result.failure(IllegalArgumentException(nombreResult.error))

        val confirmPassword = validateConfirmationPasswordUser(usuario.password, usuario.confirmPassword!!)
        if (!confirmPassword.isValid)
            return Result.failure(IllegalArgumentException(confirmPassword.error))

        return runCatching { usuarioRepository.crearUsuario(usuario.domainToDto()) }
    }
}