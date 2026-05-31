package com.appmascotasv2.smartpaws.data.repository

import com.appmascotasv2.smartpaws.data.local.dao.MascotaDao
import com.appmascotasv2.smartpaws.data.local.entity.MascotaEntity
import kotlinx.coroutines.flow.Flow

class MascotaRepository(private val MascotaDao: MascotaDao) {

    fun getPetsForUser(userId: Int): Flow<List<MascotaEntity>> =
        MascotaDao.getMascotasByOwner(userId)

    suspend fun addMascota(pet: MascotaEntity) {
        MascotaDao.insert(pet)
    }

    suspend fun updateMascota(pet: MascotaEntity) {
        MascotaDao.update(pet)
    }

    suspend fun deleteMascota(pet: MascotaEntity) {
        MascotaDao.delete(pet)
    }
}