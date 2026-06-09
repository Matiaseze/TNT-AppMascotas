package com.appmascotasv2.smartpaws.data.repository

import com.appmascotasv2.smartpaws.data.local.dao.MascotaDao
import com.appmascotasv2.smartpaws.data.mapper.toDomain
import com.appmascotasv2.smartpaws.data.mapper.toEntity
import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import com.appmascotasv2.smartpaws.domain.repository.MascotaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MascotaRepositoryImpl(private val mascotaDao: MascotaDao) : MascotaRepository {

    override fun getMascotasByOwner(ownerId: Int): Flow<List<Mascota>> =
        mascotaDao.getMascotasByOwner(ownerId).map { list -> list.map { it.toDomain() } }

    override suspend fun addMascota(mascota: Mascota) {
        mascotaDao.insert(mascota.toEntity())
    }

    override suspend fun updateMascota(mascota: Mascota) {
        mascotaDao.update(mascota.toEntity())
    }

    override suspend fun deleteMascota(mascota: Mascota) {
        mascotaDao.delete(mascota.toEntity())
    }
}