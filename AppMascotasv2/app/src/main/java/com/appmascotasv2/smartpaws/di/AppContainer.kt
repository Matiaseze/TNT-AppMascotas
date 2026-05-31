package com.appmascotasv2.smartpaws.di

import android.content.Context
import com.appmascotasv2.smartpaws.data.local.AppDatabase
import com.appmascotasv2.smartpaws.data.repository.AuthRepository
import com.appmascotasv2.smartpaws.data.repository.MascotaRepository

// Contenedor manual — reemplazable por Hilt con @Module/@Provides
class AppContainer(context: Context) {

    private val db = AppDatabase.getInstance(context)

    val authRepository = AuthRepository(db.usuarioDao(), context)
    val mascotaRepository  = MascotaRepository(db.mascotaDao())
}