package com.appmascotasv2.smartpaws.domain.usecase.auth


import com.appmascotasv2.smartpaws.domain.model.usuario.Usuario
import com.appmascotasv2.smartpaws.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): Result<Usuario> = repository.register(username, password)
}