package com.appmascotasv2.smartpaws.domain.usecase.auth

import com.appmascotasv2.smartpaws.domain.model.usuario.Usuario
import com.appmascotasv2.smartpaws.domain.repository.AuthRepository

class GetUsuarioByIdUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(id: Int): Usuario? = repository.getUsuarioById(id)

}