package com.appmascotasv2.smartpaws.di

import android.content.Context
import com.appmascotasv2.smartpaws.data.local.AppDatabase
import com.appmascotasv2.smartpaws.data.repository.AuthRepositoryImpl
import com.appmascotasv2.smartpaws.data.repository.MascotaRepositoryImpl
import com.appmascotasv2.smartpaws.domain.repository.AuthRepository
import com.appmascotasv2.smartpaws.domain.repository.MascotaRepository
import com.appmascotasv2.smartpaws.domain.usecase.mascota.AddMascotaUseCase
import com.appmascotasv2.smartpaws.domain.usecase.mascota.DeleteMascotaUseCase
import com.appmascotasv2.smartpaws.domain.usecase.mascota.GetMascotasUseCase
import com.appmascotasv2.smartpaws.domain.usecase.auth.*

// Contenedor manual — reemplazable por Hilt con @Module/@Provides
class AppContainer(context: Context) {

    private val db = AppDatabase.getInstance(context)
    // Repositorios
    private val authRepository: AuthRepository =
        AuthRepositoryImpl(db.usuarioDao(), context) // cambiar luego
    private val mascotaRepository: MascotaRepository =
        MascotaRepositoryImpl(db.mascotaDao())

    // Casos de uso — Mascota
    val getMascotasUseCase    = GetMascotasUseCase(mascotaRepository)
    val addMascotaUseCase     = AddMascotaUseCase(mascotaRepository)
    val deleteMascotaUseCase  = DeleteMascotaUseCase(mascotaRepository)

    // ── Use cases Auth ────────────────────────────────────────────────────
    val loginUseCase           = LoginUseCase(authRepository)
    val registerUseCase        = RegisterUseCase(authRepository)
    val logoutUseCase          = LogoutUseCase(authRepository)
    val getLoggedUserIdUseCase = GetLoggedUserIdUseCase(authRepository)

// Casos de uso - algo

}