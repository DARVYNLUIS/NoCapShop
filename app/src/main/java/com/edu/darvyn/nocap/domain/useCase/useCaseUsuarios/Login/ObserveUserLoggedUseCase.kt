package com.edu.darvyn.nocap.domain.useCase.useCaseUsuarios.Login

import com.edu.darvyn.nocap.domain.repository.UsuarioRepository
import javax.inject.Inject

class ObserveUserLoggedUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke() =
        usuarioRepository.getUsuario()
}