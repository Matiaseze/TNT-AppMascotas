package com.appmascotasv2.smartpaws.domain.repository

import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import kotlinx.coroutines.flow.Flow

interface MascotaRepository {
    fun getMascotasByOwner(ownerId: Int): Flow<List<Mascota>>
    suspend fun addMascota(mascota: Mascota)
    suspend fun updateMascota(mascota: Mascota)
    suspend fun deleteMascota(mascota: Mascota)
}