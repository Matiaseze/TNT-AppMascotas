package com.appmascotasv2.smartpaws.domain.usecase.auth


import com.appmascotasv2.smartpaws.domain.repository.AuthRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}