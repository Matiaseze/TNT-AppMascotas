package com.appmascotasv2.smartpaws.domain.repository


import com.appmascotasv2.smartpaws.domain.model.usuario.Usuario
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val loggedUserId: Flow<Int>
    suspend fun login(username: String, password: String): Result<Usuario>
    suspend fun register(username: String, password: String): Result<Usuario>
    suspend fun logout()

    suspend fun getUsuarioById(id: Int): Usuario?
}