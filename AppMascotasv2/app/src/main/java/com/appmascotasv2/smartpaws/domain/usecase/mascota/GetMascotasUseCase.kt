package com.appmascotasv2.smartpaws.domain.usecase.mascota


import com.appmascotasv2.smartpaws.domain.model.mascota.Mascota
import com.appmascotasv2.smartpaws.domain.repository.MascotaRepository
import kotlinx.coroutines.flow.Flow

class GetMascotasUseCase(private val repository: MascotaRepository) {
    operator fun invoke(ownerId: Int): Flow<List<Mascota>> =
        repository.getMascotasByOwner(ownerId)
}