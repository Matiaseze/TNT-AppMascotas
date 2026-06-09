package com.appmascotasv2.smartpaws.domain.usecase.auth

import com.appmascotasv2.smartpaws.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetLoggedUserIdUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<Int> = repository.loggedUserId
}