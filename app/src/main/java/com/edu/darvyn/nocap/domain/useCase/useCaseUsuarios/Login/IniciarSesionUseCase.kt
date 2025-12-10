package com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.Login

import com.edu.darvyn.nocap.data.remote.dto.RequestLogin
import com.edu.darvyn.nocap.domain.repository.UsuarioRepository
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validateEmailUser
import com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.validatePasswordUser
import javax.inject.Inject

class IniciarSesionUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(requestLogin: RequestLogin) : Result<Unit> {
        val nombreResult = validateEmailUser(requestLogin.email)
        if (!nombreResult.isValid)
            return Result.failure(IllegalArgumentException(nombreResult.error))

        val passwordResult = validatePasswordUser(requestLogin.password)
        if (!passwordResult.isValid)
            return Result.failure(IllegalArgumentException(passwordResult.error))

        return runCatching { usuarioRepository.iniciarSesion(requestLogin) }
    }
}